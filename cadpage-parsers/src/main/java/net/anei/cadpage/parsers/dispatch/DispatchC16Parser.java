package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC16Parser extends FieldProgramParser {

  public DispatchC16Parser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/1 Address:ADDRCITYST! Address_Name:PLACE! Location_Details:PLACE? CFS_Number:ID! Incident_Type:CALL/SDS! Caller:NAME! Dispatcher:SKIP? " +
              "Call_Time:DATETIME! Call_Location:SKIP! Responding_Units:UNIT? Call_Notes:INFO1! Message:INFO1/N! CFS_Latitude:GPS1! CFS_Longitude:GPS2! " +
              "Location_Alerts_(if_applicable):ALERT? " +
          "| SELECT/2 CFS#:ID! Location:ADDRCITY! ProQA_Update:INFO2! Updated_Message:SKIP! " +
          "| SELECT/3 CALL_ID ADDRCITYST3! INFO/N+ " +
          ") END");


  }

  @Override
  public String getFilter() {
    return "no-reply@csprosuite.centralsquarecloudgov.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("IAR Command Update")) {
      setSelectValue("2");
      return super.parseMsg(body, data);
    }

    else if (body.contains("\n")) {
      setSelectValue("3");
      return parseFields(body.split("\n"), data);
    }

    else {
      setSelectValue("1");
      data.strCall = subject;
      body = stripFieldEnd(body, " Please Respond Immediately.");
      int pt = body.indexOf("; ");
      if (pt >= 0 && body.substring(pt+2).startsWith("Address Name:")) {
        return parseFields(body.split("; "), data);
      } else {
        return parseMsg(body, data);
      }
    }
  }

  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST3")) return new BaseAddressCityState3Field();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("NAME")) return new BaseNameField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("INFO1")) return new BaseInfo1Field();
    if (name.equals("INFO2")) return new BaseInfo2Field();
    if (name.equals("ALERT")) return new BaseAlertField();
    if (name.equals("CALL_ID")) return new BaseCallIdField();
    return super.getField(name);
  }

  private class BaseAddressCityState3Field extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      int pt = data.strAddress.lastIndexOf(',');
      if (pt >= 0) {
        data.strPlace = data.strAddress.substring(0, pt).trim();
        data.strAddress = data.strAddress.substring(pt+1).trim();
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern CALL_ID_PTN = Pattern.compile("(.*) (\\d{4}-\\d{6})");
  private class BaseCallIdField extends BaseCallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim(), data);
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ID";
    }
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None") ||
          field.equals(data.strCall)) return;
      super.parse(field, data);
    }
  }

  private class BaseNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO1_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class BaseInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO1_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);;
    }
  }

  private static final Pattern INFO2_BRK_PTN = Pattern.compile(" +(?=Code:|Caller Statement:|\\d{1,2}\\.)");
  private class BaseInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO2_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);;
    }
  }

  private class BaseAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("* *")) return;
      super.parse(field, data);
    }
  }
}
