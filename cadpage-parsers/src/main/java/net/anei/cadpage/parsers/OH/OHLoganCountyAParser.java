package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Logan County, OH
 */
public class OHLoganCountyAParser extends DispatchCiscoParser {

  public OHLoganCountyAParser() {
    super(CITY_CODES, "LOGAN COUNTY", "OH");
    setupSpecialStreets("HARDIN HOGAN LINE");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (super.parseMsg(subject, body, data)) {
      if (data.strCity.length() == 0 && data.strMap.equals("OOOC")) data.defCity = "";
      return true;
    }
    
    if (subject.startsWith("Msg From: ")) {
      setFieldList("INFO");
      data.parseGeneralAlert(this, body);
      return true;
    }
    return false;
  }

  @Override
  public String getFilter() {
    return "lcso911@co.logan.oh.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern CITY_CORP_PTN1 = Pattern.compile("\\b([A-Z]+) (CORP|LINE|CO LINE)\\b(?! LIMIT)");
  private static final Pattern CITY_CORP_PTN2 = Pattern.compile("\\b([A-Z]+)_(CORP|LINE|CO LINE)\\b");
  private class MyCrossField extends BaseCrossField {
    @Override
    public void parse(String field, Data data) {
      field = CITY_CORP_PTN1.matcher(field).replaceAll("$1_$2");
      super.parse(field, data);
      data.strCross = CITY_CORP_PTN2.matcher(data.strCross).replaceAll("$1 $2");
    }
    
    @Override
    protected boolean parseSpecial(String field, Data data) {
      Matcher match = CITY_CORP_PTN2.matcher(field);
      if (!match.find()) return false;
      if (match.start() == 0) {
        data.strCross = append(match.group(), " / ", field.substring(match.end()).trim());
        return true;
      }
      if (match.end() == field.length()) {
        data.strCross = append(field.substring(0,match.start()).trim(), " / ",   match.group());
        return true;
      }
      return false;
    }
  }
  
  @Override
  public int getExtraParseAddressFlags() {
    return FLAG_PREF_TRAILING_DIR;
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "AVONDALE",        "LAKEVIEW",
      "BLACKHAWK",       "LAKEVIEW",
      "CHIPPEWA",        "LAKEVIEW",
      "HOL SH",          "LAKEVIEW",
      "INDIAN LAKE SHORE","BELLE CENTER",
      "INDIAN RIDGE",    "HUNTSVILLE",
      "ISLV",            "LAKEVIEW",
      "KINGS LANDING",   "LAKEVIEW",
      "LAKESIDE",        "LAKEVIEW",
      "MIDWAY",          "LAKEVIEW",
      "MILF HTS",        "LAKEVIEW",
      "MOUNDWOOD",       "RUSSELLS POINT",
      "OCONN",           "BELLE CENTER",
      "ORCHARD ISLE",    "LAKEVIEW",
      "SEMINOLE ISLE",   "HUNTSVILLE",
      "TECUMSEH ISLE",   "HUNTSVILLE",
      "TRACE OAKS",      "WEST LIBERTY",
      "TURKEYFOOT",      "LAKEVIEW",
      "WATERBURY",       "RUSSELLS POINT"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AVONDALE",  "AVONDALE",
      "BC",        "BELLE CENTER",
      "BF",        "BELLEFONTAINE",
      "BLKHWK",    "BLACKHAWK",
      "CHIPP",     "CHIPPEWA",
      "CHIPPE",    "CHIPPEWA",
      "CHIPPEWA",  "CHIPPEWA",
      "DEGRA",     "DE GRAFF",
      "DEGRAFF",   "DE GRAFF",
      "DG",        "DE GRAFF",
      "HOL SH",    "HOL SH",
      "HUNTSVILLE","HUNTSVILLE",
      "HV",        "HUNTSVILLE",
      "IN LK SH",  "INDIAN LAKE SHORE",
      "IND L",     "INDIAN LAKE SHORE",
      "IND LK SH", "INDIAN LAKE SHORE",
      "IND R",     "INDIAN RIDGE",
      "IND RDG",   "INDIAN RIDGE",
      "ISLV",      "ISLV",
      "KGS LD",    "KINGS LANDING",
      "LAKESID",   "LAKESIDE",
      "LAKESIDE",  "LAKESIDE",
      "LAKEVIEW",  "LAKEVIEW",
      "LV",        "LAKEVIEW",
      "LWTN",      "LEWISTOWN",
      "MILF HTS",  "MILF HTS",
      "MILF HT",   "MILF HTS",
      "MDBG",      "MIDDLEBURG",
      "MID",       "MIDWAY",
      "MIDWA",     "MIDWAY",
      "MIDWAY",    "MIDWAY",
      "MOUNDW",    "MOUNDWOOD",
      "MOUNDWO",   "MOUNDWOOD",
      "MWOOD",     "MOUNDWOOD",
      "OCONN",     "OCONN",
      "ORC",       "ORCHARD ISLE",
      "ORCH",      "ORCHARD ISLE",
      "ORCH I",    "ORCHARD ISLE",
      "ORCH ISL",  "ORCHARD ISLE",
      "QUINCY",    "QUINCY",
      "QY",        "QUINCY",
      "RDGW",      "RIDGEWAY",
      "RDGWY",     "RIDGEWAY",
      "RDGWAY",    "RIDGEWAY",
      "RIDGEWAY",  "RIDGEWAY",
      "RP",        "RUSSELLS POINT",
      "RUSSELLS POINT", "RUSSELLS POINT",
      "SEM",       "SEMINOLE ISLE",
      "SEM IS",    "SEMINOLE ISLE",
      "SEM I",     "SEMINOLE ISLE",
      "SEM ISL",   "SEMINOLE ISLE",
      "TF",        "TURKEYFOOT",
      "TFOOT",     "TURKEYFOOT",
      "TEC IS",    "TECUMSEH ISLE",
      "TEC ISL",   "TECUMSEH ISLE",
      "TRACE OAKS","TRACE OAKS",
      "VH",        "VALLEY HI",
      "WATERBUR",  "WATERBURY",
      "WATERBURY", "WATERBURY",
      "WEST MANSFIELD",   "WEST MANSFIELD",
      "WL",        "WEST LIBERTY",
      "WM",        "WEST MANSFIELD",
      "ZF",        "ZANESFIELD",
      "ZANESFIELD","ZANESFIELD",

      "AUGLAIZE",     "AUGLAIZE COUNTY",
      "CHAMPAIGN",    "CHAMPAIGN COUNTY",
      "HARDIN",       "HARDIN COUNTY",
      "SHELBY",       "SHELBY COUNTY",
      "UNION",        "UNION COUNTY",


  });
}
