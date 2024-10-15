package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
          "( Call_Type:CALL! | Call_Types:CALL! ) CFS#:ID? ( SELECT/RR INFO/N+ | Date:DATETIME! ) Location:ADDRCITY! Cross_Streets:X? Common_Name:PLACE! " +
                 "( SELECT/RR Additional_Location_Information:INFO/N INFO/N+? GPS " +
                 "| Agencies_Dispatched:SRC! Units_Currently_Assigned:UNIT! EMPTY+? GPS? " +
                 "| EMPTY+? CFS_ID:ID/L! CASE_NUM:SKIP! " +
                 ") " +
                 "Current_Remarks:EMPTY INFO/N+");
    this.callCodes = callCodes;
  }

  private boolean unitFldFound;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    unitFldFound = false;
    if (subject.startsWith("InterOp CAD Alert -") ||
        subject.startsWith("InterOp CAD UPDATE -")) {
      setSelectValue("");
      body = body.replace("Agency Dispatched:", "Agencies Dispatched:").replace("CFS_ID:", "CFS ID:");
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
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("GPS")) return new GPSField("Estimated Maps Location.*['\"]http://maps.apple.com/maps\\?q=([^'\"]*?)['\"].*", true);
    if (name.equals("INFO")) return new BaseInfoField();
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

  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strAddress = stripFieldEnd(data.strAddress, " APT");
      data.strAddress = stripFieldEnd(data.strAddress, ",");
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

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      unitFldFound = true;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_LINK_PTN = Pattern.compile("<img src=\"(.*?)\".*");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("CFS ADDRESS CHANGED TO .* \\| COORD LAT: (\\S+) LNG: (\\S+)");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Call Sent by ")) return;
      if (field.startsWith("UNIT:")) {
        if (!unitFldFound && field.endsWith(" - UNIT DISPATCHED")) {
          field = field.substring(5, field.length()-18).trim();
          data.strUnit = append(data.strUnit, ", ", field);
        }
      } else {
        Matcher match = INFO_LINK_PTN.matcher(field);
        if (match.matches()) {
          data.strInfoURL = match.group(1).trim();
          return;
        }

        match = INFO_GPS_PTN.matcher(field);
        if (match.matches()) {
          setGPSLoc(match.group(1)+','+match.group(2), data);
          return;
        }
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " URL UNIT GPS";
    }
  }
}

