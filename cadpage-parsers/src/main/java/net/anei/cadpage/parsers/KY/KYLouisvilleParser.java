package net.anei.cadpage.parsers.KY;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Louisville, KY
 */
public class KYLouisvilleParser extends FieldProgramParser {

  public KYLouisvilleParser() {
    super(CITY_CODES, "LOUISVILLE", "KY",
           "Location:ADDR/S? JTN:PLACE? TYPE_CODE:CODE! SUB_TYPE:CALL! TIME:TIME? Comments:INFO INFO+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "MetroSafeTech@louisvilleky.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      String call = CALL_CODES.getCodeDescription(field);
      if (call != null) data.strCall = call;
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("default")) return;
      if (field.length() == 0) return;
      if (data.strCall.length() == 0) {
        data.strCall = field;
      } else {
        data.strCall = data.strCall + " (" + field + ")";
      }
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        if (data.strPlace.startsWith("@")) data.strPlace = data.strPlace.substring(1).trim();
        field = field.substring(0,pt).trim();
      }
      Parser p = new Parser(field);
      data.strApt = append(p.getLastOptional(','), "-", p.getLastOptional(';'));
      field = p.get();
      if (field.startsWith("@")) field = field.substring(1).trim();
      super.parse(field, data);
      pt = data.strCity.indexOf('/');
      if (pt >= 0) {
        data.strPlace = append(data.strPlace, " - ", data.strCity.substring(0,pt));
        data.strCity = data.strCity.substring(pt+1);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      DispatchProQAParser.parseProQAData(false, field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }

  private static CodeTable CALL_CODES = new StandardCodeTable();

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "LEFLORE AVE",                          "+38.130719,-85.875992",
      "MISTY LN",                             "+38.130724,-85.876780",
      "TALLAHATCHEE ST",                      "+38.130719,-85.875992",
      "YAZOO ST",                             "+38.130656,-85.876378",

      "10300 LA PLAZA DR",                    "+38.406946,-85.871035",
      "10301 LA PLAZA DR",                    "+38.106763,-85.870535",
      "10302 LA PLAZA DR",                    "+38.106760,-85.871122",
      "10303 LA PLAZA DR",                    "+38.106626,-85.870619",
      "10304 LA PLAZA DR",                    "+38.106631,-85.871260",
      "10305 LA PLAZA DR",                    "+38.106483,-85.870721",
      "10306 LA PLAZA DR",                    "+38.106457,-85.871314",
      "10307 LA PLAZA DR",                    "+38.106315,-85.870824",
      "10308 LA PLAZA DR",                    "+38.106278,-85.871433",
      "10309 LA PLAZA DR",                    "+38.106124,-85.870915",
      "10310 LA PLAZA DR",                    "+38.106066,-85.871546",
      "10311 LA PLAZA DR",                    "+38.105924,-85.871123",
      "10312 LA PLAZA DR",                    "+38.105899,-85.871667",
      "10313 LA PLAZA DR",                    "+38.105693,-85.871139",
      "10400 LA PLAZA DR",                    "+38.105787,-85.871757",
      "10401 LA PLAZA DR",                    "+38.105624,-85.871256",
      "10402 LA PLAZA DR",                    "+38.105658,-85.871886",
      "10403 LA PLAZA DR",                    "+38.105487,-85.871325",
      "10404 LA PLAZA DR",                    "+38.105487,-85.871942",
      "10405 LA PLAZA DR",                    "+38.105333,-85.871441",
      "10406 LA PLAZA DR",                    "+38.105292,-85.872019",
      "10407 LA PLAZA DR",                    "+38.105154,-85.871491",
      "10408 LA PLAZA DR",                    "+38.105169,-85.872165",
      "10409 LA PLAZA DR",                    "+38.104977,-85.871567",
      "10410 LA PLAZA DR",                    "+38.105040,-85.872256",
      "10411 LA PLAZA DR",                    "+38.104849,-85.871692",
      "10412 LA PLAZA DR",                    "+38.104881,-85.872338",
      "10413 LA PLAZA DR",                    "+38.104715,-85.871785",
      "10414 LA PLAZA DR",                    "+38.104732,-85.872414",
      "10415 LA PLAZA DR",                    "+38.104563,-85.871863",
      "10416 LA PLAZA DR",                    "+38.104573,-85.872501",
      "10417 LA PLAZA DR",                    "+38.104416,-85.871978",
      "10418 LA PLAZA DR",                    "+38.104413,-85.872620",
      "10419 LA PLAZA DR",                    "+38.104199,-85.872130",
      "10620 MORAINE CIR",                    "+38.269389,-85.560232",
      "3402 SOUTHMEADE CIR",                  "+38.154698,-85.822535",
      "3403 SOUTHMEADE CIR",                  "+38.154974,-85.821994",
      "3405 SOUTHMEADE CIR",                  "+38.155102,-85.822243",
      "3406 SOUTHMEADE CIR",                  "+38.154943,-85.822783",
      "3407 SOUTHMEADE CIR",                  "+38.155269,-85.822391",
      "3408 SOUTHMEADE CIR",                  "+38.155060,-85.823010",
      "3410 SOUTHMEADE CIR",                  "+38.155224,-85.823151",
      "3412 SOUTHMEADE CIR",                  "+38.155474,-85.823063",
      "3413 SOUTHMEADE CIR",                  "+38.155535,-85.822391",
      "3414 SOUTHMEADE CIR",                  "+38.155625,-85.822946",
      "3416 SOUTHMEADE CIR",                  "+38.155817,-85.822929",
      "3417 SOUTHMEADE CIR",                  "+38.155721,-85.822405",
      "3418 SOUTHMEADE CIR",                  "+38.155956,-85.822811",
      "3419 SOUTHMEADE CIR",                  "+38.155887,-85.822212",
      "3420 SOUTHMEADE CIR",                  "+38.156176,-85.822628",
      "3422 SOUTHMEADE CIR",                  "+38.156304,-85.822456",
      "3423 SOUTHMEADE CIR",                  "+38.155976,-85.822022",
      "3424 SOUTHMEADE CIR",                  "+38.156587,-85.822271",
      "3428 SOUTHMEADE CIR",                  "+38.156505,-85.821798",
      "3429 SOUTHMEADE CIR",                  "+38.156081,-85.821825",
      "3430 SOUTHMEADE CIR",                  "+38.156474,-85.821656",
      "3431 SOUTHMEADE CIR",                  "+38.150049,-85.821487",
      "3432 SOUTHMEADE CIR",                  "+38.156417,-85.821296",
      "3433 SOUTHMEADE CIR",                  "+38.155928,-85.821332",
      "3434 SOUTHMEADE CIR",                  "+38.156346,-85.821052"

  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANC",  "ANCHORAGE",
      "AUD",  "AUDUBON PARK",
      "BAN",  "BANCROFT",
      "BAR",  "BARBOURMEADE",
      "BELM", "BELLEMEADE",
      "BELW", "BELLEWOOD",
      "BPOI", "BROECK POINTE",
      "BRFA", "BROWNSBORO FARM",
      "BRM",  "BLUE RIDGE MANOR",
      "BRVI", "BEECHWOOD VILLAGE",
      "BRWD", "BRIARWOOD",
      "BVIL", "BEECHWOOD VILLAGE",
      "CAMB", "CAMBRIDGE",
      "COLD", "COLDSTREAM",
      "CROS", "CROSSGATE",
      "CSID", "CREEKSIDE",
      "DHIL", "DOUGLASS HILLS",
      "DRHI", "DRUID HILLS",
      "FHIL", "FOREST HILLS",
      "FINC", "FINCASTLE",
      "GCRE", "GOOSE CREEK",
      "GMOR", "GRAYMOOR-DEVONDALE",
      "GSPR", "GREEN SPRING",
      "GVEW", "GLENVIEW",
      "GVHI", "GLENVIEW HILLS",
      "GVMA", "GLENVIEW MANOR",
      "HCRE", "HOLLOW CREEK",
      "HDAL", "HILLS AND DALES",
      "HHIL", "HICKORY HILL",
      "HOAC", "HOUSTON ACRES",
      "HTCK", "HERITAGE CREEK",
      "HUAC", "HURSTBOURNE ACRES",
      "HURS", "HURSTBOURNE",
      "HVIL", "HOLLYVILLA",
      "INH",  "INDIAN HILLS",
      "JTN",  "JEFFERSONTOWN",
      "KING", "KINGSLEY",
      "LINC", "LINCOLNSHIRE",
      "LPLA", "LANGDON PLACE",
      "LVIL", "LOUISVILLE",
      "LYND", "LYNDON",
      "LYNN", "LYNNVIEW",
      "MCRE", "MANOR CREEK",
      "MEDE", "MEADOWVIEW ESTATES",
      "MEDV", "MEADOWVALE",
      "MEVL", "MEADOW VALE",
      "MFAR", "MEADOWBROOK FARM",
      "MHES", "MARYHILL ESTATES",
      "MHIL", "MURRAY HILL",
      "MOOR", "MOORLAND",
      "MOVL", "MOCKINGBIRD VALLEY",
      "MTWN", "MIDDLETOWN",
      "MVES", "MEADOWVIEW ESTATES",
      "NEST", "NORBOURNE ESTATES",
      "NORT", "NORTHFIELD",
      "NOWD", "NORWOOD",
      "OBPL", "OLD BROWNSBORO PLACE",
      "PHIL", "POPLAR HILLS",
      "PLAN", "PLANTATION",
      "PRO",  "PROSPECT",
      "PWVI", "PARKWAY VILLAGE",
      "RFIE", "ROLLING FIELDS",
      "RHIL", "ROLLING HILLS",
      "RICH", "RICHLAWN",
      "RVWD", "RIVERWOOD",
      "SGAR", "SENECA GARDENS",
      "SHV",  "SHIVELY",
      "SMAN", "STRATHMOOR MANOR",
      "SMIL", "SPRING MILL",
      "SPVW", "SOUTH PARK VIEW",
      "SRPK", "ST REGIS PARK",
      "STM",  "ST MATTHEWS",
      "SVAL", "SPRING VALLEY",
      "SVIL", "STRATHMOOR VILLAGE",
      "SYCA", "SYCAMORE",
      "TBRO", "TEN BROECK",
      "THIL", "THORNHILL",
      "UNK",  "UNKNOWN",
      "WB",   "WEST BUECHEL",
      "WDHI", "WOODLAND HILLS",
      "WDPK", "WOODLAWN PARK",
      "WDWD", "WILDWOOD",
      "WELL", "WELLINGTON",
      "WHIL", "WINDY HILLS",
      "WORH", "WORTHINGTON HILLS",
      "WTPK", "WATTERSON PARK",
      "WTWD", "WESTWOOD"
  });
}
