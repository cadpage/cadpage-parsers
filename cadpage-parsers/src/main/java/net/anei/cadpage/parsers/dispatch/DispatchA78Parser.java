package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA78Parser extends FieldProgramParser {

  public DispatchA78Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Type:CALL! Date:DATETIME! Location:ADDRCITY! Cross_Streets:X! Common_Name:PLACE! Agencies_Dispatched:SRC! Units_Currently_Assigned:UNIT! EMPTY+? GPS?");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("InterOp CAD Alert - ")) return false;
    body = body.replace("Agency Dispatched:", "Agencies Dispatched:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("GPS")) return new GPSField("Estimated Maps Location.*['\"]http://maps.apple.com/maps\\?q=([^'\"]*?)['\"].*", true);
    return super.getField(name);
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "||");
      super.parse(field, data);
    }
  }

}

