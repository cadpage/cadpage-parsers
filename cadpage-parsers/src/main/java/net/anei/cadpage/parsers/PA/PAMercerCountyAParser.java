package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA50Parser;

/**
 * Mercer County, PA
 */
public class PAMercerCountyAParser extends DispatchA50Parser {

  public PAMercerCountyAParser() {
    super(CALL_CODES, null, "MERCER COUNTY", "PA");
    setupCities(CITY_CODES);
    setupCities(REG_CITY_LIST);
    setupCities(OHIO_CITIES);
    setupSpecialStreets("HAYES ORANGEVILLE");
  }

  @Override
  public String getFilter() {
    return "@mcc.co.mercer.pa.us,911-CENTER@leoc.net";
  }

  private static final Pattern CO_911_PTN = Pattern.compile("(.*?)[- ]+9-?1-?1");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("IPS I/Page Notification")) return false;
    if (!super.parseMsg(body, data)) return false;

    boolean inCounty = data.strCity.length() == 4;
    data.strCity = convertCodes(data.strCity, CITY_CODES);

    if (data.strCity.length() == 0) {
      String name = data.strName.toUpperCase();
      Matcher match = CO_911_PTN.matcher(name);
      if (match.matches()) name = match.group(1);
      if (name.endsWith(" CO")) {
        data.strCity = name + "UNTY";
      } else if (COUNTY_SET.contains(name)) {
        data.strCity = name + " COUNTY";
      } else if (name.equals("CC911")) {
        data.strCity = "CRAWFORD COUNTY";
      }
    }
    int pt = data.strAddress.indexOf(':');
    if (pt >= 0) {
      data.strCity = data.strAddress.substring(pt+1).trim();
      data.strAddress = data.strAddress.substring(0,pt).trim();
    }
    if (!inCounty & OHIO_CITIES.contains(data.strCity.toUpperCase())) data.strState = "OH";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Set<String> COUNTY_SET = new HashSet<String>(Arrays.asList(
      "BUTLER",
      "CRAWFORD",
      "LAWRENCE",
      "MAHONING",
      "TRUMBULL",
      "VENANGO"
  ));

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLRK",  "CLARK",
      "COOL",  "COOLSPRING TWP",
      "DEER",  "DEER CREEK TWP",
      "DELA",  "DELAWARE TWP",
      "ELAC",  "EAST LACKAWANNOCK TWP",
      "FAIR",  "FAIRVIEW TWP",
      "FARL",  "FARRELL",
      "FIND",  "FINDLEY TWP",
      "FRED",  "FREDONIA",
      "FREN",  "FRENCH CREEK TWP",
      "GRNE",  "GREENE TWP",
      "GRVL",  "GREENVILLE",
      "GROV",  "GROVE CITY",
      "HEMP",  "HEMPFIELD TWP",
      "HERM",  "HERMITAGE",
      "JACK",  "JACKSON TWP",
      "JCEN",  "JACKSON CENTER",
      "JAME",  "JAMESTOWN",
      "JEFF",  "JEFFERSON TWP",
      "LACK",  "LACKAWANNOCK TWP",
      "LAKE",  "LAKE TWP",
      "LIBR",  "LIBERTY TWP",
      "MERC",  "MERCER",
      "MILL",  "MILL CREEK TWP",
      "NLEB",  "NEW LEBANON",
      "NVER",  "NEW VERNON TWP",
      "OTTR",  "OTTER CREEK TWP",
      "PERR",  "PERRY TWP",
      "PINE",  "PINE TWP",
      "PYMA",  "PYMATUNING TWP",
      "SALM",  "SALEM TWP",
      "SCRK",  "SANDY CREEK TWP",
      "SLAK",  "SANDY LAKE",
      "SLTP",  "SANDY LAKE TWP",
      "SHAR",  "SHARON",
      "SHRP",  "SHARPSVILLE",
      "SKVL",  "SHEAKLEYVILLE",
      "SHEN",  "SHENANGO TWP",
      "SPYM",  "SOUTH PYMATUNING TWP",
      "SPRG",  "SPRINGFIELD TWP",
      "STON",  "STONEBORO",
      "SUGR",  "SUGAR GROVE",
      "WMID",  "WEST MIDDLESEX",
      "WSAL",  "WEST SALEM TWP",
      "WHEA",  "WHEATLAND",
      "WILM",  "WILMINGTON TWP",
      "WOLF",  "WOLF CREEK TWP",
      "WRTH",  "WORTH TWP",

      "COOLSPRING",  "COOLSPRING TWP",
      "DEER CREEK",  "DEER CREEK TWP",
      "DELAWARE",    "DELAWARE TWP",
      "EAST LACKAWANNOCK",  "EAST LACKAWANNOCK TWP",
      "FAIRVIEW",    "FAIRVIEW TWP",
      "FINDLEY",     "FINDLEY TWP",
      "FRENCH CREEK","FRENCH CREEK TWP",
      "GREENE",      "GREENE TWP",
      "HEMPFIELD",   "HEMPFIELD TWP",
      "JACKSON",     "JACKSON TWP",
      "JEFFERSON",   "JEFFERSON TWP",
      "LACKAWANNOCK", "LACKAWANNOCK TWP",
      "LAKE",        "LAKE TWP",
      "LIBERTY",     "LIBERTY TWP",
      "MILL CREEK",  "MILL CREEK TWP",
      "NEW VERNON",  "NEW VERNON TWP",
      "OTTER CREEK", "OTTER CREEK TWP",
      "PERRY",       "PERRY TWP",
      "PYMATUNING",  "PYMATUNING TWP",
      "SALEM",       "SALEM TWP",
      "SANDY CREEK", "SANDY CREEK TWP",
      "SHENANGO",    "SHENANGO TWP",
      "SOUTH PYMATUNING", "SOUTH PYMATUNING TWP",
      "SPRINGFIELD", "SPRINTFIELD TWP",
      "SUGAR GROVE", "SUGAR GROVE TWP",
      "WEST SALEM",  "WEST SALEM TWP",
      "WILMINGTON",  "WILMINGTON TWP",
      "WOLF CREEK",  "WOLF CREEK TWP",
      "WORTH",       "WORTH TWP"
  });

  private static final String[] REG_CITY_LIST = new String[]{

      "FARRELL",
      "HERMITAGE",
      "SHARON",

      "CLARK",
      "FREDONIA",
      "GREENVILLE",
      "GROVE CITY",
      "JACKSON CENTER",
      "JAMESTOWN",
      "MERCER",
      "NEW LEBANON",
      "SANDY LAKE",
      "SHAPRSVILLE",
      "SHEAKLEYVILLE",
      "STONEBORO",
      "WEST MIDDLESEX",
      "WHEATLAND",

      // Crawford County
      "ADAMSVILLE"
  };

  private static final Set<String> OHIO_CITIES = new HashSet<String>(Arrays.asList(
      "BUTLER COUNTY",
      "BUTLER CO",

      "CHERRY TWP",

      "TRUMBLE COUNTY",
      "TRUMBLE CO",

      "CORTLAND",
      "GIRARD",
      "HUBBARD",
      "NEWTON FALLS",
      "NILES",
      "WARREN",
      "YOUNGSTOWN",

      "LORDSTOWN",
      "MCDONALD",
      "ORANGEVILLE",
      "WEST FARMINGTON",
      "YANKEE LAKE",

      "BAZETTA",
      "BLOOMFIELD",
      "BRACEVILLE",
      "BRISTOL",
      "BROOKFIELD",
      "CHAMPION",
      "FARMINGTON",
      "FOWLER",
      "GREENE",
      "GUSTAVUS",
      "HARTFORD",
      "HOWLAND",
      "HUBBARD",
      "JOHNSTON",
      "KINSMAN",
      "LIBERTY",
      "MECCA",
      "MESOPOTAMIA",
      "NEWTON",
      "SOUTHINGTON",
      "VERNON",
      "VIENNA",
      "WARREN",
      "WEATHERSFIELD",

      "BAZETTA TWP",
      "BLOOMFIELD TWP",
      "BRACEVILLE TWP",
      "BRISTOL TWP",
      "BROOKFIELD TWP",
      "CHAMPION TWP",
      "FARMINGTON TWP",
      "FOWLER TWP",
      "GREENE TWP",       // Possible confusion with GREENE TWP in Mercer County
      "GUSTAVUS TWP",
      "HARTFORD TWP",
      "HOWLAND TWP",
      "HUBBARD TWP",
      "JOHNSTON TWP",
      "KINSMAN TWP",
      "LIBERTY TWP",
      "MECCA TWP",
      "MESOPOTAMIA TWP",
      "NEWTON TWP",
      "SOUTHINGTON TWP",
      "VERNON TWP",
      "VIENNA TWP",
      "WARREN TWP",
      "WEATHERSFIELD TWP",

      "LORDSTOWN TOWNSHIP",

      "BOLINDALE",
      "BROOKFIELD CENTER",
      "CHAMPION HEIGHTS",
      "CHURCHILL",
      "HILLTOP",
      "HOWLAND CENTER",
      "LEAVITTSBURG",
      "MAPLEWOOD PARK",
      "MASURY",
      "MINERAL RIDGE",
      "SOUTH CANAL",
      "VIENNA CENTER",
      "WEST HILL",

      "BRISTOLVILLE",
      "BURGHILL",
      "CENTER OF THE WORLD",
      "FARMDALE",
      "FOWLER",
      "HARTFORD",
      "KINSMAN",
      "NORTH BLOOMFIELD",
      "SOUTHINGTON"
  ));

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "AF",   "Fire-Admin Activity",
      "ANML", "Animal Rescue",
      "BACK", "Back Pains",
      "BARN", "Barn Fire",
      "BRU",  "Brush Fire",
      "CHIM", "Chimney Fire",
      "CMD",  "Carbon Monoxide Detector",
      "CMDS", "Carbon Monoxide detector w/ symptoms",
      "COM",  "Commercial Fire",
      "ELI",  "Electric Fire",
      "ELO",  "Electric Lines down",
      "EXP",  "Explosion",
      "FALM", "Fire alarm",
      "GARC", "Comercial Garage Fire",
      "GARF", "Garbage Fire",
      "GAS",  "Inside Gas Leak",
      "HAZ",  "Hazmat",
      "HF",   "House fire",
      "KITF", "Kitchen Fire",
      "MHF",  "Mobile Home Fire",
      "MUT",  "Mutual Aid",
      "MVA",  "Motor Vehicle Crash w. no injuries",
      "MVE",  "Motor Vehicle Crash w. trap",
      "MVI",  "Motor Vehicle Crash w/ injuries",
      "MVU",  "Motor Vehicle Crash w/ unknown injuries",
      "RES",  "Rescue Call",
      "ROAD", "Notification of Road Conditions",
      "QRS",  "EMS Call",
      "SAR",  "Search and Rescue",
      "SDET", "Smoke Detector Activation",
      "SERV", "Service Call",
      "SMOI", "Smoke in Structure",
      "STR",  "Structure Fire",
      "TREE", "Tree down",
      "UNKF", "Unknown Type Fire",
      "UTL",  "Utility Lines Down"

      // Unknown codes
      // ABD, ALLER, ASST, BLEED, BOMB, BURN, CHEST, DIAB, DMV, DROWN, FALL, FADM, FUEL, HEAD, HEART, INJP, MISP, MVF, ODR, OOS, PSYCH, PUMP, SICK, SEIZE, SMO, TEST, TRAS
  });
}
