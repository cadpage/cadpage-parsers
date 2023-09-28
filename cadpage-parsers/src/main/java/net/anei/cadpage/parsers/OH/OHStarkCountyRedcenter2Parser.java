package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class OHStarkCountyRedcenter2Parser extends FieldProgramParser {

  public OHStarkCountyRedcenter2Parser() {
    super("STARK COUNTY", "OH",
          "( Call:CALL! Date/Time:DATETIME1! ( Place:PLACE! Address:ADDRCITY! | Address:ADDRCITY! Place:PLACE ) Crosses:X! Latitude:GPS1? Longitude:GPS2? Section:MAP? Run_Num:ID? Unit:SKIP? Alert:INFO1! Info:INFO1% INFO1/N+ " +
                "( MAC_Channel:CH Units_Assigned:UNIT Run_Number:ID | Map:TIMES1+ ) " +
          "| Address:ADDRCITY! Grid:MAP! Cross_Streets:X! Nature_Of_Call:CALL! DATETIME1! Incident_Number:ID! SKIP! INFO+? TIMES1! TIMES1+ " +
          "| Call_Address:ADDRCITY! Radio_Channel:CH! Common_Name:PLACE! Qualifier:EMPTY! Cross_Streets:X Local_Information:INFO! Custom_Layer:SKIP! Census_Tract:EMPTY! Call_Type:CALL! Call_Priority:PRI! Call_Date/Time:DATETIME1? Nature_Of_Call:CALL/SDS! Units_Assigned:UNIT! Fire_Quadrant:MAP! Incident_Number(s):ID! Caller_Name:NAME! Caller_Phone:PHONE! Caller_Address:CADDR! Alerts:SKIP! Narratives:INFO1! Status_Times:TIMES1+ Google_Maps_Hyperlink:SKIP " +
          "| CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! XST:X? ( ID:ID! PRI:PRI? DATE:DATETIME1! MAP:MAP_X! UNIT:SKIP? INFO:INFO1! TIMES1+ " +
                                                          "| CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT? INFO:INFO ) " +
          "| INC_ID DATE:DATE! TIME:TIME! BLDG:PLACE! LOC:ADDRCITY! APT:APT! XST:X! XST:X? TRU:UNIT! NAT:CALL! NOTES:INFO/N+ )");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "@starksheriff.org,@redcenter.us,@neo-comm.org,7127390583,messaging@iamresponding.com,@cantonohio.gov,@ci.minerva.oh.us,starkcountycad@gmail.com,777";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  String unit;
  String times;

  private static final Pattern JUNK_BRK_PTN = Pattern.compile("\n *~ *\n|, *\n");
  private static final Pattern DELIM = Pattern.compile("\n(?: +\n)*");

  @Override
  protected boolean parseMsg(String body, Data data) {
    unit = null;
    times = "";
    body = JUNK_BRK_PTN.matcher(body).replaceAll("\n");
    if (!parseFields(DELIM.split(body), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new MyDateTime1Field();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("TIMES1")) return new MyTimes1Field();
    if (name.equals("CADDR")) return new MyCallerAddressField();
    if (name.equals("MAP_X")) return new MyMapCrossField();

    if (name.equals("INC_ID")) return new IdField("INC# *(.*)", true);
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();

    return super.getField(name);
  }

  private static final Pattern DATE_TIME1_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?);?");
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTime1Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT1, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private static final Pattern ADDR_CITY_APT_PTN = Pattern.compile("(.*?), +([^,]*?)(?: +(FL [A-Z0-9 ]*|[A-Z0-9]*[0-9][A-Z0-9]*|[A-Z]))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_APT_PTN.matcher(field);
      if (match.matches()) {
        String addr = match.group(1);
        data.strCity = getOptGroup(match.group(2));
        String apt = match.group(3);

        if (apt != null) addr = stripFieldEnd(addr, ' '+apt);
        parseAddress(addr, data);
        if (apt != null) data.strApt = append(data.strApt, "-", apt);
      } else {
        parseAddress(field, data);
      }
    }
  }

  private static final Pattern ID_PTN = Pattern.compile("\\[ *([- 0-9]+?) *\\]");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(",")) {
        part = part.trim();
        Matcher match = ID_PTN.matcher(part);
        if (match.matches()) {
          data.strCallId = append(data.strCallId, "/", match.group(1));
        }
      }
    }
  }

  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("[","").replace("]","");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private class MyTimes1Field extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("Unit:")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Unit:")) {
        unit = field.substring(5).trim();
        data.strUnit = append(data.strUnit, " ", unit);
        return;
      }
      if (unit == null) return;
      if (field.length() == 0) return;
      if (field.startsWith("Cleared at:")) data.msgType = MsgType.RUN_REPORT;
      field = unit + "  " + field;
      times = append(times, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }
  }

  private class MyCallerAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.length() > 0) return;
      super.parse(field, data);
    }
  }

  private class MyMapCrossField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCross = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      data.strMap = field;
    }

    @Override
    public String getFieldNames() {
      return "MAP X";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String code = p.getLast(' ');
      String city = CITY_CODES.getProperty(code);
      if (city != null) {
        data.strCity = city;
        field = p.get();
      }
      if (field.equalsIgnoreCase("No cross streets found")) return;
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "X CITY";
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "4232 GRAPH RD NW",                     "+40.590590,-81.179056"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "NW-GT",   "GREENTOWN"
  });
}
