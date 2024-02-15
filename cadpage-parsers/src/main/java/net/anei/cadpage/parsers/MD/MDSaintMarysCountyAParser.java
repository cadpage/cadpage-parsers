package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDSaintMarysCountyAParser extends FieldProgramParser {

  public MDSaintMarysCountyAParser() {
    super(CITY_LIST, "SAINT MARYS COUNTY", "MD",
          "TIME CALL ( DEMPTY EMPTY+? UNIT CALL/SDS! | PLACE/Z ADDR/Z X/Z X/Z CITY! | ADDR/Z X/Z CITY! X/Z+? UNIT PLACE | ADDR/Z X/Z CITY/Z X/Z UNIT! PLACE ) EMPTY? INFO/N+ END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupProtectedNames("BARNES AND YEH", "LAKE AND BRETON VIEW DR");
  }

  @Override
  public String getFilter() {
    return "mplus@co.saint-marys.md.us,mplus@STMARYSMD.COM,@stmaryscountymd.gov,777,888";
  }

  private static final Pattern LEAD_NUMBER_PTN = Pattern.compile("\\d+ .*");
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\\*", -1), data)) return false;
    if (data.strCall.equals("Road Closure") &&
        !LEAD_NUMBER_PTN.matcher(data.strAddress).matches() &&
        LEAD_NUMBER_PTN.matcher(data.strCross).matches()) {
      String addr = data.strCross;
      data.strCross = data.strAddress;
      data.strAddress = addr;
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CLOSURE")) return new CallField("Road Closure", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DEMPTY")) return new MyDoubleEmptyField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+[A-Z]?|ALS|DNR|ECCR?|FDC|MDO|PUI|SMCPD|SMECO|TFER|USCG|WEA|WXWARN|WXWAT)\\b ?)+", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyDoubleEmptyField extends EmptyField {
    @Override
    public boolean checkParse(String field, Data data) {
      return field.isEmpty() && getRelativeField(+1).isEmpty();
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile(".* (?:APT|RM|ROOM|UNIT) *\\S+");
  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      if (!isPlaceField(field, getRelativeField(+1))) return false;
      super.parse(field, data);
      return true;
    }

    private boolean isPlaceField(String field1, String field2) {
      // Place field is optional, and if present is always followed by the address field.
      // What makes things complicated is that the place field can sometimes be a street
      // name, and the address field will probably be followed by a cross street.

      // If this field ends with APTS, assume it is a place
      if (field1.endsWith(" APTS")) return true;

      // They frequently use an apartment convention that the dumb address parser recognizes
      // but the smart address parser does not.  If we find it in either field, make that
      // the address
      if (ADDR_APT_PTN.matcher(field1).matches()) return false;
      if (ADDR_APT_PTN.matcher(field2).matches()) return true;

      // If the current field is a full address or intersection, it is not a place field
      int status1 = checkAddress(field1);
      if (status1 >= STATUS_INTERSECTION) return false;

      // If the next field is a full address or intersection, this must be a place field
      int status2 = checkAddress(field2);
      if (status2 >= STATUS_INTERSECTION) return true;

      // OK, now things get dicey.  If second field is a street name, assume the first is
      // a malformed address (ie not place)
      if (status2 == STATUS_STREET_NAME) return false;

      // Out of ideas :( Just pick the best field to be the address.  If this
      // field isn't it, it must be the place field
      return status2 > status1;
    }
  }

  private static final Pattern CO_UNIT_PTN = Pattern.compile("CO\\d+");
  private class MyCrossField extends CrossField {

    @Override
    public boolean checkParse(String field, Data data) {

      // They have some unit names that get confused with county roads, so
      // reject them out of hand
      if (CO_UNIT_PTN.matcher(field).matches()) return false;
      return super.checkParse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +(?=\\d{1,2}\\. )");
  private class MyInfoField extends InfoField {
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      data.strSupp = append(data.strSupp, "*", field);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, CITY_FIX_TABLE);
  }

  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    if (address.equals("46860 HILTON DR") && apt.length() > 2) {
      address = address + " BLDG " + apt.substring(0, apt.length()-2);
    }
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "45325 ABELL HOUSE LN",                 "+38.298550,-76.508292",
      "44100A LOUISDALE RD",                  "+38.294415,-76.558088",
      "44100B LOUISDALE RD",                  "+38.293484,-76.558780",
      "44605 BARNES AND YEH LANE",            "+38.289400,-76.532300",
      "44655 BARNES AND YEH LANE",            "+38.286800,-76.530900",
      "44665 BARNES AND YEH LANE",            "+38.286400,-76.531600",
      "44683 BARNES AND YEH LANE",            "+38.285600,-76.530600",
      "46155 CARVER SCHOOL BL",               "+38.250002,-76.480090",
      "43950 CASEYS LN",                      "+38.283806,-76.556252",
      "25390 COLTON POINT RD",                "+38.360118,-76.706042",
      "48017 CROSS MANOR RD",                 "+38.159970,-76.414850",
      "20845 DAISY LN",                       "+38.237791,-76.460179",
      "23520 FDR BOULEVARD",                  "+38.303844,-76.524341",
      "23550 FDR BOULEVARD",                  "+38.304432,-76.524862",
      "23580 FDR BOULEVARD",                  "+38.305154,-76.525273",
      "45910 FOXCHASE DR",                    "+38.255517,-76.489115",
      "45915 FOXCHASE DR",                    "+38.255538,-76.488573",
      "45925 FOXCHASE DR",                    "+38.255976,-76.488343",
      "45935 FOXCHASE DR",                    "+38.256480,-76.488265",
      "45955 FOXCHASE DR",                    "+38.256263,-76.489099",
      "45965 FOXCHASE DR",                    "+38.256115,-76.489767",
      "45975 FOXCHASE DR",                    "+38.255842,-76.490476",
      "45985 FOXCHASE DR",                    "+38.255207,-76.490182",
      "21412 GREAT MILLS RD BWING",           "+38.254868,-76.477345",
      "45298 HAPPYLAND RD",                   "+38.207787,-76.508714",
      "46860 HILTON DR BLDG 1",               "+38.247680,-76.459357",
      "46860 HILTON DR BLDG 2",               "+38.248052,-76.459638",
      "46860 HILTON DR BLDG 3",               "+38.248016,-76.460045",
      "46860 HILTON DR BLDG 4",               "+38.247862,-76.460461",
      "46860 HILTON DR BLDG 5",               "+38.248137,-76.460772",
      "46860 HILTON DR BLDG 6",               "+38.248310,-76.460351",
      "46860 HILTON DR BLDG 7",               "+38.248618,-76.460261",
      "46860 HILTON DR BLDG 8",               "+38.248898,-76.460566",
      "46860 HILTON DR BLDG 9",               "+38.249052,-76.460147",
      "46860 HILTON DR BLDG 10",              "+38.248774,-76.459841",
      "46860 HILTON DR BLDG 11",              "+38.248637,-76.459482",
      "46860 HILTON DR BLDG 12",              "+38.248869,-76.459048",
      "46860 HILTON DR BLDG 13",              "+38.246921,-76.455179",
      "46860 HILTON DR BLDG 14",              "+38.246892,-76.455582",
      "46860 HILTON DR BLDG 15",              "+38.246974,-76.456037",
      "46860 HILTON DR BLDG 16",              "+38.247192,-76.456301",
      "46860 HILTON DR BLDG 17",              "+38.247367,-76.456849",
      "46860 HILTON DR BLDG 18",              "+38.247198,-76.457187",
      "46860 HILTON DR BLDG 19",              "+38.247560,-76.457808",
      "46860 HILTON DR BLDG 20",              "+38.247340,-76.458201",
      "46860 HILTON DR BLDG 21",              "+38.247626,-76.458444",
      "46860 HILTON DR BLDG 22",              "+38.249001,-76.456838",
      "46860 HILTON DR BLDG 23",              "+38.248483,-76.456992",
      "46860 HILTON DR BLDG 24",              "+38.248172,-76.456569",
      "46860 HILTON DR BLDG 25",              "+38.247810,-76.456274",
      "46860 HILTON DR BLDG 26",              "+38.248028,-76.455913",
      "46860 HILTON DR BLDG 27",              "+38.248600,-76.455898",
      "46860 HILTON DR BLDG 28",              "+38.248697,-76.456372",
      "46860 HILTON DR BLDG 29",              "+38.248491,-76.455268",
      "46860 HILTON DR BLDG 30",              "+38.248461,-76.454921",
      "46860 HILTON DR BLDG 31",              "+38.248002,-76.455438",
      "46860 HILTON DR BLDG 32",              "+38.247738,-76.455525",
      "46860 HILTON DR BLDG 33",              "+38.247667,-76.455166",
      "46860 HILTON DR BLDG 34",              "+38.247891,-76.454839",
      "46860 HILTON DR BLDG 35",              "+38.248175,-76.454757",
      "22492 JOHNSON POND LANE",              "+38.285500,-76.549000",
      "22494 JOHNSON POND LANE",              "+38.285500,-76.549000",
      "22505 JOHNSON POND LANE",              "+38.286000,-76.550400",
      "22525 JOHNSON POND LANE",              "+38.286300,-76.550800",
      "22550 JOHNSON POND LANE",              "+38.286500,-76.550700",
      "45889 KETCH COURT",                    "+38.242582,-76.487140",
      "44096 LOUISDALE RD",                   "+38.296261,-76.562480",
      "44100 LOUISDALE RD",                   "+38.295197,-76.558400",
      "45770 MILITARY LANE",                  "+38.282760,-76.492340",
      "45772 MILITARY LANE",                  "+38.282890,-76.492300",
      "45774 MILITARY LANE",                  "+38.283020,-76.492440",
      "45776 MILITARY LANE",                  "+38.283240,-76.492330",
      "45778 MILITARY LANE",                  "+38.283340,-76.492180",
      "45780 MILITARY LANE",                  "+38.283340,-76.491810",
      "45782 MILITARY LANE",                  "+38.283200,-76.491060",
      "45790 MILITARY LANE",                  "+38.282720,-76.493240",
      "45792 MILITARY LANE",                  "+38.282850,-76.493030",
      "44994 NORRIS RD",                      "+38.273770,-76.507290",
      "44800 OAK CREST RD",                   "+38.311297,-76.527188",
      "22024 OXFORD CT",                      "+38.273351,-76.468718",
      "22025 OXFORD CT",                      "+38.273123,-76.468245",
      "22026 OXFORD CT",                      "+38.273393,-76.468547",
      "22027 OXFORD CT",                      "+38.273178,-76.468065",
      "22028 OXFORD CT",                      "+38.273434,-76.468369",
      "22030 OXFORD CT",                      "+38.273478,-76.468200",
      "48676 PACKER CT",                      "+38.155650,-76.391350",
      "23060 PARK PLACE WAY",                 "+38.299107,-76.510502",
      "17814 PINEY POINT RD",                 "+38.157170,-76.520433",
      "43738 STEPHENSON DR",                  "+38.198215,-76.563438",
      "42831 THERESAS WY",                    "+38.368228,-76.595153",
      "22589 THREE NOTCH RD",                 "+38.287556,-76.480120",
      "45462 WEST MEATH WAY",                 "+38.288942,-76.503772",
      "45472 WEST MEATH WAY",                 "+38.288764,-76.503479",
      "45482 WEST MEATH WAY",                 "+38.288690,-76.503104",
      "45485 WEST MEATH WAY",                 "+38.288198,-76.503089",
      "45495 WEST MEATH WAY",                 "+38.288101,-76.502741",
      "45502 WEST MEATH WAY",                 "+38.288475,-76.502375",
      "45511 WEST MEATH WAY",                 "+38.287958,-76.502099",
      "45512 WEST MEATH WAY",                 "+38.288355,-76.502034",
      "45521 WEST MEATH WAY",                 "+38.287701,-76.502262",
      "45522 WEST MEATH WAY",                 "+38.288337,-76.501659"
  });

  private static String[] CITY_LIST = new String[]{
      "CALIFORNIA",
      "CEDAR COVE",
      "CEDARCOVE",
      "CHAR HALL",
      "CHARLOTTE HALL",
      "CHESTNUT HILLS",
      "GOLDEN BEACH",
      "LEXINGTON PARK",
      "ABELL",
      "AVENUE",
      "BAREFOOT ACRES",
      "BEACHVILLE-ST INIGOES",
      "BUSHWOOD",
      "CALLAWAY",
      "CHAPTICO",
      "CLEMENTS",
      "COLTONS POINT",
      "COUNTRY LAKES",
      "COMPTON",
      "DAMERON",
      "DRAYDEN",
      "ESPERANZA FARMS",
      "FIRST COLONY",
      "GLEN FOREST NAWC",
      "GREAT MILLS",
      "HELEN",
      "HERMANVILLE",
      "HOLLYWOOD",
      "LAUREL GROVE",
      "LEONARDTOWN",
      "LEXINGTON PARK",
      "LORD CALVERT TRLPK",
      "LOVEVILLE",
      "MADDOX",
      "MECHANICSVILLE",
      "MEDLEYS NECK",
      "MORGANZA",
      "NEW MARKET",
      "OAKVILLE",
      "ORAVILLE",
      "PARK HALL",
      "PINEY POINT",
      "REDGATE",
      "RIDGE",
      "SAN SOUCI",
      "SCOTLAND",
      "SOUTH HAMPTON",
      "SPRING RIDGE",
      "SAINT CLEMENTS WOODS",
      "ST CLEMENTS WOODS",
      "ST INIGOES",
      "ST JAMES",
      "ST MARYS CITY",
      "TALL TIMBERS",
      "THOMPSONS CORNER",
      "TOWN CREEK",
      "VALLEY LEE",
      "WILDEWOOD",

      "CALVERT COUNTY"
  };

  private static final Properties CITY_FIX_TABLE = buildCodeTable(new String[]{
      "CHAR HALL", "CHARLOTTE HALL",

      "BAREFOOT ACRES", "CALIFORNIA",
      "ESPERANZA FARMS","CALIFORNIA",
      "FIRST COLONY",   "CALIFORNIA",
      "SAN SOUCI",      "CALIFORNIA",
      "TOWN CREEK",     "CALIFORNIA",

      "CEDAR COVE",   "LEXINGTON PARK",
      "CEDARCOVE",    "LEXINGTON PARK",
      "GLEN FOREST NAWC", "LEXINGTON PARK",
      "LORD CALVERT TRLPK", "LEXINGTON PARK",
      "HERMANVILLE",  "LEXINGTON PARK",
      "SOUTH HAMPTON","LEXINGTON PARK",
      "SPRING RIDGE", "LEXINGTON PARK",
      "ST JAMES",     "LEXINGTON PARK",

      "MEDLEYS NECK",          "LEONARDTOWN",
      "SAINTT CLEMENTS WOODS", "LEONARDTOWN",
      "ST CLEMENTS WOODS",     "LEONARDTOWN",

      "THOMPSONS CORNER",      "MECHANICSVILLE"
  });
}
