package net.anei.cadpage.parsers.OR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORUmatillaCountyBParser extends HtmlProgramParser {

  public ORUmatillaCountyBParser() {
    this("UMATILLA COUNTY", "OR");
  }

  public ORUmatillaCountyBParser(String defCity, String defState) {
    super(defCity, defState,
          "JUNK+? " +
            "( CALL_DATE_/_TIME:DATETIME! " +
                    "( FIRE_CALL_TYPE:CALL! EMS_CALL_TYPE:CALL! COMMON_NAME:PLACE! ADDRESS:ADDRCITY/S6! APT/SUITE:APT! CROSS_STREET:X! " +
                        "FIRE_QUAD:MAP! NATURE_OF_CALL:CALL/SDS! DISPATCH_COMMENTS:EMPTY! INFO/N+ LOCAL_INFO:INFO/N! INFO/N+ ALERTS:ALERT! INFO/N+ " +
                        "INCIDENT_#:ID! UNITS:UNIT! STATUS_TIMES:EMPTY! SKIP+ CALLER_NAME:NAME! CALLER_PHONE_#:PHONE! CALLER_ADDRESS:SKIP! " +
                    "| COMMON_NAME:PLACE! ADDRESS:ADDRCITY/S6! APT/SUITE:APT! CROSS_STREET:X! LATITUDE:GPS? " +
                        "FIRE_CALL_TYPE:CALL/SDS? FIRE_CALL_DESCRIPTION:CALL/SDS? EMS_CALL_TYPE:CALL/SDS? EMS_CALL_DESCRIPTION:CALL/SDS? " +
                        "CALL_TYPE:CALL/SDS? CALL_DESCRIPTON:CALL/SDS? NATURE_OF_CALL:CALL/SDS! " +
                        "DISPATCH_COMMENTS:EMPTY! INFO/N+ LOCAL_INFO:INFO/N? INFO/N+ ALERTS:ALERT! INCIDENT_#:ID! SKIP+ " +
                        "CALLER_NAME:NAME! ( CALLER_PHONE_#:PHONE! | CALLER_PHONE:PHONE! ) CALLER_ADDRESS:SKIP! FIRE_CALL_TYPE:CALL/SDS? FIRE_CALL_DESCRIPTION:CALL/SDS? " +
                        "EMS_CALL_TYPE:CALL/SDS? EMS_CALL_DESCRIPTION:CALL/SDS? FIRE_QUADRANT:MAP? EMS_DISTRICT:MAP/L? " +
                        "FIRE_RADIO_CHANNEL:CH? EMS_RADIO_CHANEL:CH/L? " +
                    ") " +
            "| Incident_Number:ID! Apt/Suite:APT! Call_Date_and_Time:SKIP! Common_Name:PLACE! Fire_Call_Type:CODE! Address:ADDRCITY/S6! Cross_Streets:X! Fire_Quadrant:MAP! " +
                "Caller_Name:NAME! Caller_Phone:PHONE! Caller_Address:SKIP! Narative:INFO! INFO/N+ Alerts:ALERT! ALERT/N+ Unit_Assigned:UNIT! Assigned_Station:SRC? " +
                "Unit:SKIP? Dispatched:DATETIME? Map_Link:EMPTY! GPS2! " +
            ")");

  }

  @Override
  public String getFilter() {
    return "@milton-freewater-or.gov,RipnRunReport@wallawallawa.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MISSED_BRK_PTN = Pattern.compile(" +(?=Apt/Suite:|Call Date and Time:|Fire Quadrant:|Caller Phone:)");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification")) return false;
    body = MISSED_BRK_PTN.matcher(body).replaceAll("</div><div>");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("JUNK")) return new SkipField("(?!FIRE CALL TYPE:|Incident Number:).*", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("GPS2")) return new GPSField("https://www.google.com.*query=(.*)", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strApt)) return;
      super.parse(field,  data);;
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("[Incident not yet created")) return;
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" LONGITUDE:", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_HEADER_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}|\\d\\d?:\\d\\d:\\d\\d");
  private class MyInfoField extends InfoField {

    private boolean suppressHeader = false;

    @Override
    public void parse(String field, Data data) {
      if (!suppressHeader) {
        if (INFO_HEADER_PTN.matcher(field).matches()) {
          suppressHeader = true;
          return;
        }
      } else {
        if (field.equals("-")) suppressHeader = false;
        return;
      }

      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
