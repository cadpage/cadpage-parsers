package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class OHGreeneCountyAParser extends FieldProgramParser {

  private static final Pattern MISSED_BLANK_PTN = Pattern.compile("([^ ])(Info:)");

  public OHGreeneCountyAParser() {
    super(CITY_LIST, "GREENE COUNTY", "OH",
          "( SELECT/1 ( Call:CALL! Name:NAME! Address:ADDR_CITY_X_PLACE! Cross:X! Units:UNIT! Call_Time:DATETIME! Dispatch_Time:EMPTY! Quadrant:MAP! Radio_Channel:CH! Alerts:ALERT! " +
                     "| CALL! Location:ADDR_CITY_X_PLACE! Time:DATETIME1! Units:UNIT! Common_Name:PLACE/SDS! Quadrant:MAP1! Primary_Incident:ID! " +
                     ") Narrative:INFO/N INFO/N+ " +
          "| CALL2 Location:ADDR2/SXXx! Time:TIME Units:UNIT Common_Name:PLACE Info:INFO ( Problem:CALL Patient_Info:INFO | Nature_Of_Call:CALL ) Incident_#:ID2 Narrative:INFO Nature_Of_Call:CALL/SDS Quadrant:MAP EMS_District:MAP )");
  }

  @Override
  public String getFilter() {
    return "@ci.xenia.oh.us,PSISN_Dispatch@GreeneCoOHPSISN.gov";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Call Type:");
    String[] flds = body.split("\n");
    if (flds.length > 5) {
      setSelectValue("1");
      if (!parseFields(flds, data)) return false;
    } else {
      setSelectValue("2");
      body = MISSED_BLANK_PTN.matcher(body).replaceAll("$1 $2");
      if (!super.parseMsg(body, data)) return false;
    }
    if (data.strCity.equals("CAESARCREEK TWP")) data.strCity = "CAESARSCREEK TWP";
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR_CITY_X_PLACE")) return new MyAddressCityCrossPlaceField();
    if (name.equals("DATETIME1")) return new MyDateTime1Field();
    if (name.equals("MAP1")) return new MyMap1Field();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("ID2")) return new MyIdInfo2Field();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_CROSS_PLACE_PTN = Pattern.compile("([^,~]*)(?:, ([^~]*?))? ~ ([^~]*)(?: ~ ([^~]*))?");
  private static final Pattern ADDR_X_STREETS_PTN = Pattern.compile(" *\\bX STREETS\\b *");
  private class MyAddressCityCrossPlaceField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("~")) field += ' ';
      Matcher match = ADDR_CITY_CROSS_PLACE_PTN.matcher(field);
      if (!match.matches()) abort();
      parseAddress(match.group(1).trim().replace('@', '&'), data);
      data.strCity = getOptGroup(match.group(2));
      String g3 = match.group(3).trim();
      String g4 = getOptGroup(match.group(4));
      match = ADDR_X_STREETS_PTN.matcher(g4);
      if (match.find()) {
        data.strApt = append(data.strApt, "-", g3);
        if (data.strCity.length() == 0) data.strCity = g4.substring(0, match.start());
        data.strCross = g4.substring(match.end());
      } else {
        data.strCross = g3;
        data.strPlace = g4;
      }
      if (data.strCross.equals("No Cross Streets Found")) data.strCross = "";
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X PLACE";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTime1Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(2), data);
      } else {
        data.strTime = time;
      }
    }
  }

  private class MyMap1Field extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      super.parse(field, data);
    }
  }

  private static final Pattern MM_PTN = Pattern.compile("\\d+MM");
  private class MyAddress2Field extends AddressField {

    @Override
    public void parse(String field, Data data) {

      super.parse(field, data);
      if (MM_PTN.matcher(data.strApt).matches()) {
        data.strAddress = append(data.strAddress, " ", data.strApt);
        data.strApt = "";
      }
      if (data.strCross.equalsIgnoreCase("No Cross Streets Found")) data.strCross = "";
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE X CITY";
    }
  }

  private static Pattern ID_INFO_PTN = Pattern.compile("(\\d+-\\d+) *(.*)");
  private class MyIdInfo2Field extends MyInfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_INFO_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1);
        field = match.group(2).trim();
      }
      DispatchProQAParser.parseProQAData(false, field, data);
    }

    @Override
    public String getFieldNames() {
      return "ID " + super.getFieldNames();
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      DispatchProQAParser.parseProQAData(false, field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO CODE";
    }
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BEAVERCREEK",
    "BELLBROOK",
    "CENTERVILLE",
    "FAIRBORN",
    "HUBER HEIGHTS",
    "KETTERING",
    "XENIA",

    // Villages
    "BOWERSVILLE",
    "CEDARVILLE",
    "CLIFTON",
    "JAMESTOWN",
    "SPRING VALLEY",
    "YELLOW SPRINGS",

    // Townships
    "BATH TWP",
    "BEAVERCREEK TWP",
    "CAESARCREEK TWP",
    "CEDARVILLE TWP",
    "JEFFERSON TWP",
    "MIAMI TWP",
    "NEW JASPER TWP",
    "ROSS TWP",
    "SILVERCREEK TWP",
    "SPRING VALLEY TWP",
    "SUGARCREEK TWP",
    "XENIA TWP",

    //Other
    "SHAWNEE HILLS",
    "WILBERFORCE",
    "WRIGHT-PATTERSON AIR FORCE BASE",
    "BYRON",
    "OLDTOWN",

    "CLARK COUNTY",
    "CLINTON COUNTY",
    "FAYETTE COUNTY",
    "MADISON COUNTY",
    "MIAMI COUNTY",
    "MONTGOMERY COUNTY",
    "WARREN COUNTY"

  };
}
