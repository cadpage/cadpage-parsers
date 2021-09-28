package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class DispatchA78Parser extends FieldProgramParser {

  private final Properties callCodes;

  public DispatchA78Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA78Parser(Properties callCodes, String defCity, String defState) {
    super(defCity, defState,
          "Call_Type:CALL! ( SELECT/RR INFO/N+ | Date:DATETIME! ) Location:ADDRCITY! Cross_Streets:X! Common_Name:PLACE! " +
                 "( SELECT/RR Additional_Location_Information:INFO/N! INFO/N+? GPS | Agencies_Dispatched:SRC! Units_Currently_Assigned:UNIT! EMPTY+? GPS? )");
    this.callCodes = callCodes;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("InterOp CAD Alert - ")) {
      setSelectValue("");
      body = body.replace("Agency Dispatched:", "Agencies Dispatched:");
      return parseFields(body.split("\n"), data);
    }

    if (subject.startsWith("InterOp CAD - CALL Completed -")) {
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("RR");
      return parseFields(body.split("\n"), data);
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("GPS")) return new GPSField("Estimated Maps Location.*['\"]http://maps.apple.com/maps\\?q=([^'\"]*?)['\"].*", true);
    return super.getField(name);
  }

  private class BaseCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (callCodes == null) {
        data.strCall = field;
      } else {
        data.strCode = field;
        String call = callCodes.getProperty(field);
        if (call == null) call = field;
        data.strCall = call;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }

  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("||", "/");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

}

