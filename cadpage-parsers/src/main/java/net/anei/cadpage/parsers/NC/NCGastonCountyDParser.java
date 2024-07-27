package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class NCGastonCountyDParser extends DispatchH05Parser {

  public NCGastonCountyDParser() {
    super("GASTON COUNTY", "NC",
          "MARKER! Location_Information:EMPTY! Location:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! ( Fire_Quadrant:MAP! Fire_Radio_Channel:CH! | EMS_District:MAP! | ) " +
              "Call_Number:SKIP! ( Fire_Call_Type:CALL! | EMS_Call_Type:CALL! | ) Nature_of_Call:CALL/SDS! Call_Date/Time:DATETIME! Caller:NAME! " +
              "( Status_Times:EMPTY! TIMES+ Incident_Numbers:EMPTY! ID! Units_Sent:EMPTY! UNIT! Alerts:EMPTY! ALERT+ Narrative:EMPTY! INFO_BLK+ " +
              "| Units_Sent:EMPTY! UNIT! Narrative:EMPTY! INFO_BLK+ Status_Times:EMPTY! TIMES+ Alerts:EMPTY! ALERT+ " +
              ") END");
  }

  @Override
  public String getFilter() {
    return "donotreply@gcps.org";
  }

  private String alertInfo;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    alertInfo = "";
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.msgType != MsgType.RUN_REPORT && !alertInfo.isEmpty()) {
      data.strAlert = "** Location Alert **";
      data.strSupp = append(alertInfo, "\n", data.strSupp);
    }
    alertInfo = null;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARKER")) return new SkipField("Rip & Run Report", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d");
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("ALERT")) return new MyAlertField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
      if (data.strCity.equals("COUNTY")) data.strCity = "";
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }

  private Pattern ALERT_JUNK_PTN = Pattern.compile("- *\\d+ *-");
  private class MyAlertField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (ALERT_JUNK_PTN.matcher(field).matches()) return;
      if (alertInfo.contains(field)) return;
      alertInfo = append(alertInfo, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "ALERT INFO";
    }
  }
}
