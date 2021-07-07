package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


/**
 * Allegan County, MI
 */
public class MIAlleganCountyParser extends DispatchH05Parser {

  public MIAlleganCountyParser() {
    super(CITY_LIST, "ALLEGAN COUNTY", "MI",
          "( SELECT/1 DATETIME! Call_Type:CALL! Call_Location:ADDRCITY/S6! Cross_Streets:X! Add_Loc_Info:INFO! Caller:NAME! Units:UNIT! Incident_Number:ID! Narrative:INFO/N+ " +
          "| Date_&_Time:DATETIME! Call_Type:CALL! Priority:PRI! Call_Location:ADDRCITY/S6! Cross_Streets:X! Caller_Name_&_Phone_Number:NAME_PHONE! Units:UNIT! Incident_Number:ID! Narrative:EMPTY! INFO_BLK+ Unit_Times:EMPTY TIMES+ )");
          setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "incidentnotification@allegancounty.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.equals("Dispatch")) {
      setSelectValue("1");
      return parseMsg(body, data);
    } else {
      setSelectValue("2");
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    return super.getField(name);
  }

  private static final Pattern LEAD_HWY_PTN = Pattern.compile("(HWY)\\b *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRAIL_CITY_PTN = Pattern.compile("(.*?) +(?:CITY|VILLAGE)", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field.substring(0,pt).trim(), data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field.substring(pt+1).trim(), data);
        if (data.strCity.isEmpty()) {
          data.strCity = getLeft();
        } else {
          data.strPlace = getLeft();
        }

        Matcher match = LEAD_HWY_PTN.matcher(data.strApt);
        if (match.matches()) {
          data.strAddress = append(data.strAddress, " ", match.group(1));
          data.strApt = match.group(2);
        }
      } else {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT, field, data);
        data.strPlace = getLeft();
        Matcher match = LEAD_HWY_PTN.matcher(data.strPlace);
        if (match.matches()) {
          data.strAddress = append(data.strAddress, " ", match.group(1));
          data.strPlace = match.group(2);
        }
      }

      Matcher match = TRAIL_CITY_PTN.matcher(data.strCity);
      if (match.matches()) data.strCity = match.group(1);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*) (\\(\\d{3}\\) ?\\d{3}-\\d{4})");
  private class MyNamePhoneField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPhone = match.group(2);
      }
      data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return stripFieldEnd(address, " HWY");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "34 NB I 196", "42.59336,-86.21648",
      "34 SB I 196", "42.59336,-86.21688",
      "35 NB I 196", "42.60745,-86.21864",
      "35 SB I 196", "42.60745,-86.21900",
      "36 NB I 196", "42.62398,-86.21078",
      "36 SB I 196", "42.62398,-86.21128",
      "37 NB I 196", "42.63197,-86.19904",
      "37 SB I 196", "42.63197,-86.19955",
      "38 NB I 196", "42.64425,-86.18860",
      "38 SB I 196", "42.64425,-86.18897",
      "39 NB I 196", "42.65541,-86.18390",
      "39 SB I 196", "42.65541,-86.18426",
      "40 NB I 196", "42.66707,-86.17190",
      "40 SB I 196", "42.66707,-86.17292",
      "41 NB I 196", "42.68180,-86.16984",
      "41 SB I 196", "42.68180,-86.17020",
  });

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "ALLEGAN",
      "ALLEGAN CITY",
      "DOUGLAS",
      "DOUGLAS CITY",
      "FENNVILLE",
      "FENNVILLE CITY",
      "HOLLAND",
      "HOLLAND CITY",
      "OTSEGO",
      "OTSEGO CITY",
      "PLAINWELL",
      "PLAINWELL CITY",
      "SAUGATUCK",
      "SAUGATUCK CITY",
      "SOUTH HAVEN",
      "SOUTH HAVEN CITY",
      "WAYLAND",
      "WAYLAND CITY",

      // Villages
      "HOPKINS",
      "HOPKINS VILLAGE",
      "MARTIN",
      "MARTIN VILLAGE",

      // Unincorporated communities
      "ARGENTA",
      "BAKERSVILLE",
      "BEACHMONT",
      "BENTHEIM",
      "BELKNAP",
      "BRADLEY",
      "BRAVO",
      "BOYD",
      "BURNIPS",
      "CASTLE PARK",
      "CEDAR BLUFF",
      "CHESHIRE",
      "CHICORA",
      "CORNING",
      "DIAMOND SPRINGS",
      "DORR",
      "DUNNINGVILLE",
      "EAST SAUGATUCK",
      "EAST MARTIN",
      "FILLMORE",
      "GANGES",
      "GLENN",
      "GLENN HAVEN SHORES",
      "GLENN SHORES",
      "GRAAFSCHAP",
      "GRANGE CORNERS",
      "GREEN LAKE",
      "HAMILTON",
      "HAWKHEAD",
      "HILLIARDS",
      "HOOPER",
      "HOPKINSBURG",
      "KIBBIE",
      "LACOTA",
      "LEE",
      "LEISURE",
      "MACATAWA",
      "MACKS LANDING",
      "MERSON",
      "MIAMI PARK",
      "MILLGROVE",
      "MOLINE",
      "MONTEITH STATION",
      "MONTEREY",
      "MONTEREY CENTER",
      "MOUNT PLEASANT",
      "NEELEY",
      "NEW SALEM",
      "NEW RICHMOND",
      "OLD SAUGATUCK",
      "OLD SQUAW SKIN LANDING",
      "OVERISEL",
      "OXBOW",
      "PEARL",
      "PIER COVE",
      "PLUMMERVILLE",
      "PULLMAN",
      "SANDY PINES",
      "SHELBYVILLE",
      "SHERMAN PARK",
      "SHORECREST",
      "SHOREWOOD",
      "SOUTH HAVEN HIGHLANDS",
      "SOUTH MONTEREY",
      "SPRING GROVE",
      "SULPHUR SPRINGS",
      "WATSON",

      // Townships
      "ALLEGAN TWP",
      "CASCO TWP",
      "CHESHIRE TWP",
      "CLYDE TWP",
      "DORR TWP",
      "FILLMORE TWP",
      "GANGES TWP",
      "GUN PLAIN TWP",
      "HEATH TWP",
      "HOPKINS TWP",
      "LAKETOWN TWP",
      "LEE TWP",
      "LEIGHTON TWP",
      "MANLIUS TWP",
      "MARTIN TWP",
      "MONTEREY TWP",
      "OTSEGO TWP",
      "OVERISEL TWP",
      "SALEM TWP",
      "SAUGATUCK TWP",
      "TROWBRIDGE TWP",
      "VALLEY TWP",
      "WATSON TWP",
      "WAYLAND TWP",

      // Neighboring counties
      "BARRY CO",
      "KALAMAZOO CO",
      "KENT CO",
      "OTTAWA CO",
      "VAN BUREN CO"
  };

}
