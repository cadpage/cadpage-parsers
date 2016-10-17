package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

/**
 * Adams County, PA
 */
public class PAAdamsCountyAParser extends DispatchA1Parser {
  
  public PAAdamsCountyAParser() {
    super(CITY_LIST, "ADAMS COUNTY", "PA");
    for (String city : CITY_LIST) {
      if (city.endsWith(" BORO")) setupCities(city.substring(0,city.length()-5));
    }
    setupCities(MD_CITIES);
    setupCities(MISTYPED_CITIES);
    addExtendedDirections();
  }

  @Override
  public String getFilter() {
    return "adams911@adamscounty.us,messaging@iamresponding.com,777";
  }
  
  private static final Pattern IAMR_PREFIX1 = Pattern.compile("^(?:Alert: +)?(.*?)[ \n](?=ALRM LVL:|: +BOX )");
  private static final Pattern IAMR_BOX_PTN = Pattern.compile("[, ] +BOX ");
  private static final Pattern IAMR_COMMA_PTN = Pattern.compile("[ ,]*\n[ ,]*");
  private static final Pattern TOWNSHIP_PTN = Pattern.compile("\\bTOWNSHIP\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Check for garbled prefix associated with IamResponding
    Matcher match = IAMR_PREFIX1.matcher(body);
    if (match.lookingAt()) {
      data.strSource = subject;
      subject = "Alert: " + match.group(1).trim();
      body = body.substring(match.end()).trim();
      if (body.startsWith(":")) {
        body = "RUN CARD:" + body.substring(1);
      } else {
        body = IAMR_BOX_PTN.matcher(body).replaceFirst(", RUN CARD: BOX ");
      }
      body = IAMR_COMMA_PTN.matcher(body).replaceAll("\n");
      body = body.replaceAll(" , ", " ");
    }
    
    body = TOWNSHIP_PTN.matcher(body).replaceAll("TWP");
    if (!super.parseMsg(subject, body, data)) return false;
    
    // See if a doubled city name has been interpretted as an apt
    String apt = data.strApt;
    if (data.strCity.length() == 0) {
      int pt = apt.indexOf(" - ");
      if (pt >= 0) {
        String part1 = apt.substring(0,pt).trim();
        String part2 = apt.substring(pt+2).trim();
        if (part1.endsWith("COUNTY")) part1 = part2;
        if (isCity(part1)) {
          data.strCity = part1;
          data.strApt = "";
        }
      }
    }
    
    String city = data.strCity.toUpperCase();
    city = stripFieldEnd(city, " BORO");
    city = convertCodes(city, MISTYPED_CITIES);
    if (city.endsWith(" CO")) city += "UNTY";
    data.strCity = city;
    if (MD_CITIES.contains(city)) data.strState = "MD";
    
    data.strSupp = data.strSupp.replace(" / ", "\n");
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram().replace("CITY", "CITY ST");
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_RECHECK_APT;
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Boroughs
    "ABBOTTSTOWN BORO",
    "ARENDTSVILLE BORO",
    "BENDERSVILLE BORO",
    "BIGLERVILLE BORO",
    "BONNEAUVILLE BORO",
    "CARROLL VALLEY BORO",
    "EAST BERLIN BORO",
    "FAIRFIELD BORO",
    "GETTYSBURG BORO",
    "LITTLESTOWN BORO",
    "MCSHERRYSTOWN BORO",
    "NEW OXFORD BORO",
    "YORK SPRINGS BORO",

    // Townships
    "BERWICK TWP",
    "BUTLER TWP",
    "CONEWAGO TWP",
    "CUMBERLAND TWP",
    "FRANKLIN TWP",
    "FREEDOM TWP",
    "GERMANY TWP",
    "HAMILTON TWP",
    "HAMILTONBAN TWP",
    "HIGHLAND TWP",
    "HUNTINGTON TWP",
    "LATIMORE TWP",
    "LETTERKENNY TWP",
    "LIBERTY TWP",
    "MENALLEN TWP",
    "MOUNT JOY TWP",
    "MOUNT PLEASANT TWP",
    "OXFORD TWP",
    "READING TWP",
    "STRABAN TWP",
    "TYRONE TWP",
    "UNION TWP",

    // Census-designated places
    "ASPERS",
    "CASHTOWN",
    "FLORADALE",
    "GARDNERS",
    "HAMPTON",
    "HEIDLERSBURG",
    "HUNTERSTOWN",
    "IDAVILLE",
    "LAKE HERITAGE",
    "LAKE MEADE",
    "MIDWAY",
    "MCKNIGHTSTOWN",
    "ORRTANNA",
    "TABLE ROCK",
    
    // Cumberland County
    "CUMBERLAND COUNTY",
    "CUMBERLAND CO",
    
    // Boroughs
    "CAMP HILL BORO",
    "CARLISLE BORO",
    "LEMOYNE BORO",
    "MECHANICSBURG BORO",
    "MOUNT HOLLY SPRINGS BORO",
    "MT HOLLY SPRINGS BORO",
    "NEW CUMBERLAND BORO",
    "NEWBURG BORO",
    "NEWVILLE BORO",
    "SHIREMANSTOWN BORO",
    "WORMLEYSBURG BORO",

    // Townships
    "COOKE TWP",
    "DICKINSON TWP",
    "EAST PENNSBORO TWP",
    "HAMPDEN TWP",
    "HOPEWELL TWP",
    "LOWER ALLEN TWP",
    "LOWER FRANKFORD TWP",
    "LOWER MIFFLIN TWP",
    "MIDDLESEX TWP",
    "MONROE TWP",
    "NORTH MIDDLETON TWP",
    "NORTH NEWTON TWP",
    "PENN TWP",
    "SHIPPENSBURG TWP",
    "SILVER SPRING TWP",
    "SOUTH MIDDLETON TWP",
    "SOUTH NEWTON TWP",
    "SOUTHAMPTON TWP",
    "UPPER ALLEN TWP",
    "UPPER FRANKFORD TWP",
    "UPPER MIFFLIN TWP",
    "WEST PENNSBORO TWP",

    // Census-designated places
    "BOILING SPRINGS",
    "ENOLA",
    "LOWER ALLEN",
    "MESSIAH COLLEGE",
    "NEW KINGSTOWN",
    "PLAINFIELD",
    "SCHLUSSER",
    "WEST FAIRVIEW",

    // Unincorporated communities
    "BLOSERVILLE",
    "GRANTHAM",
    "SUMMERDALE",
    "LISBURN",
    
    // Franklin County
    "FRANKLIN COUNTY",
    "FRANKLIN CO",
    
    // Boroughs
    "CHAMBERSBURG BORO",
    "GREENCASTLE BORO",
    "MERCERSBURG BORO",
    "MONT ALTO BORO",
    "ORRSTOWN BORO",
    "SHIPPENSBURG BORO",
    "WAYNESBORO BORO",

    // Townships
    "ANTRIM TWP",
    "FANNETT TWP",
    "GREENE TWP",
    "GUILFORD TWP",
    "HAMILTON TWP",
    "LETTERKENNY TWP",
    "LURGAN TWP",
    "METAL TWP",
    "MONTGOMERY TWP",
    "PETERS TWP",
    "QUINCY TWP",
    "SOUTHAMPTON TWP",
    "ST. THOMAS TWP",
    "WARREN TWP",
    "WASHINGTON TWP",

    // Census-designated places
    "BLUE RIDGE SUMMIT",
    "FAYETTEVILLE",
    "FORT LOUDON",
    "GUILFORD",
    "MARION",
    "PEN MAR",
    "ROUZERVILLE",
    "SCOTLAND",
    "STATE LINE",
    "WAYNE HEIGHTS",
    
    // York County
    "YORK COUNTY",
    "YORK CO",
    
    // City
    "YORK",

    // Boroughs
    "CROSS ROADS BORO",
    "DALLASTOWN BORO",
    "DELTA BORO",
    "DILLSBURG BORO",
    "DOVER BORO",
    "EAST PROSPECT BORO",
    "FAWN GROVE BORO",
    "FELTON BORO",
    "FRANKLINTOWN BORO",
    "FRANKLINTOWN",
    "GLEN ROCK BORO",
    "GOLDSBORO BORO",
    "HALLAM BORO",
    "HANOVER BORO",
    "JACOBUS BORO",
    "JEFFERSON BORO",
    "LEWISBERRY BORO",
    "LOGANVILLE BORO",
    "MANCHESTER BORO",
    "MOUNT WOLF BORO",
    "NEW FREEDOM BORO",
    "NEW SALEM BORO",
    "NORTH YORK BORO",
    "RAILROAD BORO",
    "RED LION BORO",
    "SEVEN VALLEYS BORO",
    "SHREWSBURY BORO",
    "SPRING GROVE BORO",
    "STEWARTSTOWN BORO",
    "WELLSVILLE BORO",
    "WEST YORK BORO",
    "WINDSOR BORO",
    "WINTERSTOWN BORO",
    "WRIGHTSVILLE BORO",
    "YOE BORO",
    "YORK HAVEN BORO",
    "YORKANA BORO",

    // Townships
    "CARROLL TWP",
    "CHANCEFORD TWP",
    "CODORUS TWP",
    "CONEWAGO TWP",
    "DOVER TWP",
    "EAST HOPEWELL TWP",
    "EAST MANCHESTER TWP",
    "FAIRVIEW TWP",
    "FAWN TWP",
    "FRANKLIN TWP",
    "HEIDELBERG TWP",
    "HELLAM TWP",
    "HOPEWELL TWP",
    "JACKSON TWP",
    "LOWER CHANCEFORD TWP",
    "LOWER WINDSOR TWP",
    "MANCHESTER TWP",
    "MANHEIM TWP",
    "MONAGHAN TWP",
    "NEWBERRY TWP",
    "NORTH CODORUS TWP",
    "NORTH HOPEWELL TWP",
    "PARADISE TWP",
    "PEACH BOTTOM TWP",
    "PENN TWP",
    "SHREWSBURY TWP",
    "SPRING GARDEN TWP",
    "SPRINGETTSBURY TWP",
    "SPRINGFIELD TWP",
    "WARRINGTON TWP",
    "WASHINGTON TWP",
    "WEST MANCHESTER TWP",
    "WEST MANHEIM TWP",
    "WINDSOR TWP",
    "YORK TWP",

    // Census-designated places
    "EAST YORK",
    "EMIGSVILLE",
    "GRANTLEY",
    "NEW MARKET",
    "PARKVILLE",
    "PENNVILLE",
    "QUEENS GATE",
    "SHILOH",
    "SPRY",
    "STONYBROOK",
    "SUSQUEHANNA TRAILS",
    "TYLER RUN",
    "VALLEY GREEN",
    "VALLEY VIEW",
    "WEIGELSTOWN",
    "YORKLYN",

    // Unincorporated communities
    "ACCOMAC",
    "ADMIRE",
    "AIRVILLE",
    "AMBAU",
    "BANDANNA",
    "BERMUDIAN",
    "BIG MOUNTAIN",
    "BLACKROCK",
    "BROGUE",
    "BRYANSVILLE",
    "CLY",
    "CRALEY",
    "DAVIDSBURG",
    "DETTERS MILL",
    "FAYFIELD",
    "FIRESIDE TERRACE",
    "FOUSTOWN",
    "FUHRMANS MILL",
    "GATCHELLVILLE",
    "GLADES",
    "GLENVILLE",
    "GNATSTOWN",
    "HAMETOWN",
    "HANOVER JUNCTION",
    "HOPEWELL CENTER",
    "KRALLTOWN",
    "LEADERS HEIGHTS",
    "MACKEY FORD",
    "MOUNT ROYAL",
    "NEW BRIDGEVILLE",
    "NEW PARK",
    "NAUVOO",
    "ORE VALLEY",
    "PORTERS SIDELING",
    "SIDDONSBURG",
    "SPRING FORGE",
    "STOVERSTOWN",
    "STRINESTOWN",
    "SUNNYBURN",
    "TOLNA",
    "THOMASVILLE",
    "VALLEY FORGE",
    "VIOLET HILL",
    "WOODBINE"


  };
  
  private static final Set<String> MD_CITIES = new HashSet<String>(Arrays.asList(
      
    // Carroll County  
    "CARROLL COUNTY",
    "CARROLL CO",
    
    // Cities
    "WESTMINSTER",
    "MOUNT AIRY",

    // Towns
    "MANCHESTER",
    "NEW WINDSOR",
    "UNION BRIDGE",
    "HAMPSTEAD",
    "SYKESVILLE",
    "TANEYTOWN",

    // Census-designated place
    "ELDERSBURG",

    // Unincorporated communities
    "ALESIA",
    "CARROLLTON",
    "CARROLLTOWNE",
    "DETOUR",
    "FINKSBURG",
    "FRIZZELBURG",
    "GAMBER",
    "GAITHER",
    "GREENMOUNT",
    "HARNEY",
    "HENRYTON",
    "JASONTOWN",
    "KEYMAR",
    "LINEBORO",
    "LINWOOD",
    "LOUISVILLE",
    "MAYBERRY",
    "MIDDLEBURG",
    "MILLERS",
    "PATAPSCO",
    "PLEASANT VALLEY",
    "SILVER RUN",
    "UNION MILLS",
    "UNIONTOWN",
    "YOUNG MANS FANCY",

    
    // Frederick County
    "FREDERICK COUNTY",
    "FREDERICK CO",
    
    // Cities
    "BRUNSWICK",
    "FREDERICK",

    // Towns
    "BURKITTSVILLE",
    "EMMITSBURG",
    "MIDDLETOWN",
    "MYERSVILLE",
    "NEW MARKET",
    "THURMONT",
    "WALKERSVILLE",
    "WOODSBORO",

    // Village
    "ROSEMONT",

    // Census-designated places
    "ADAMSTOWN",
    "BALLENGER CREEK",
    "BARTONSVILLE",
    "BRADDOCK HEIGHTS",
    "BUCKEYSTOWN",
    "JEFFERSON",
    "LIBERTYTOWN",
    "LINGANORE",
    "MONROVIA",
    "POINT OF ROCKS",
    "SABILLASVILLE",
    "SPRING RIDGE",
    "URBANA",

    // Unincorporated communities
    "CLOVER HILL",
    "DISCOVERY",
    "GARFIELD",
    "GRACEHAM",
    "GREEN VALLEY",
    "IJAMSVILLE",
    "KNOXVILLE",
    "LADIESBURG",
    "LEWISTOWN",
    "LAKE LINGANORE",
    "LINGANORE",
    "NEW MIDWAY",
    "PETERSVILLE",
    "ROCKY RIDGE",
    "SPRING GARDEN",
    "SUNNY SIDE",
    "TUSCARORA",
    "UTICA",
    "WOLFSVILLE",

    // Washington County
    "WASHINGTON COUNTY",
    "WASHINGTON CO",
    
    "BOONSBORO",
    "CASCADE",
    "CLEAR SPRING",
    "FUNKSTOWN",
    "HANCOCK",
    "HAGERSTOWN",
    "KEEDYSVILLE",
    "SHARPSBURG",
    "SMITHSBURG",
    "WILLIAMSPORT"
  ));
  
  private static final Properties MISTYPED_CITIES = buildCodeTable(new String[]{
    "BERWICK",         "BERWICK TWP",
    "CARROL TWP",      "CARROLL TWP",
    "CICKINSON TWP",   "DICKINSON TWP",
    "COOK TWP",        "COOKE TWP",
    "DICKSON TWP",     "DICKINSON TWP",
    "EMITTSBURG",      "EMMITSBURG",
    "EMMITTSBURG",     "EMMITSBURG",
    "EMTTISBURG",      "EMMITSBURG",
    "GILFORD",         "GUILFORD TWP",
    "GILFORD TWP",     "GUILFORD TWP",
    "GREEN TWP",       "GREENE TWP",
    "HEIDELBURG TWP",  "HEIDELBERG TWP",
    "HEIDLEBERG TWP",  "HEIDELBERG TWP",
    "HEIDLEBURG TWP",  "HEIDELBERG TWP",
    "MONT ALOT",       "MONT ALTO",
    "MOUNT ALTO",      "MONT ALTO",
    "MOUNT HOLLY",     "MOUNT HOLLY SPRINGS",
    "MOUNT HOLLY BORO", "MOUNT HOLLY SPRINGS",
    "PARADISE",        "PARADISE TWP",
    "QUINCEY TWP",     "QUINCY TWP",
    "ROCKEY RIDGE",    "ROCKY RIDGE",
    "SOUTHMIDDLETON TWP", "SOUTH MIDDLETON TWP",
    "WAS",             "WASHINGTON TWP",
    "WASHTINGTON TWP", "WASHINGTON TWP",
    "WASHINTON TWP",   "WASHINGTON TWP",
    "WASHTONTON TWP",  "WASHINGTON TWP",
    "WEST MANHEIM",    "WEST MANHEIM TWP"
  });
}
