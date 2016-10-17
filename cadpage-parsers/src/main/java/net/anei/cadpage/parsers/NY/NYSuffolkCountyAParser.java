package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYSuffolkCountyAParser extends SmartAddressParser {
  
  public NYSuffolkCountyAParser() {
    super(CITY_TABLE, "SUFFOLK COUNTY", "NY");
    setFieldList("CALL ADDR CITY PLACE APT X CODE INFO TIME");
    setupDoctorNames("KAHN", "HSU", "KAMDAR", "KLEINER", "SINGH");
  }
  
  @Override
  public String getFilter() {
    return "paging@scfres.com,@communityamb.org,FRES CAD,6316640853@pm.sprint.com";
  }

  private static final Pattern SUFFOLK_E_MARKER = Pattern.compile("(?:/[A-Z ]*RELAY */|(?:FROM )?RELAY )");
  private static final String[] KEYWORDS = new String[]{"TYPE", "LOC", "CROSS", "CODE", "TIME"};

  private static final Pattern CALL_ADDR_SPLIT_PTN = Pattern.compile(" +: +| {2,}");
  private static final Pattern APT_PTN = Pattern.compile("(.*)[: ](?:APT|ROOM|UNIT|STE\\b|SUITE|#)(?!S) *#?([-A-Z0-9]+?)[- ]*");
  private static final Pattern PLACE_MARK_PTN = Pattern.compile(": ?@|@|:|;");
  private static final Pattern ADDR_CROSS_PTN = Pattern.compile("(.*)(?:[ :][SC]/S(?: ?=)?| X-| CX )(.*?)(?:\\.{2,} *(.*))?");
  private static final Pattern SPECIAL_PTN = Pattern.compile("(.*)(\\*\\*\\*_[_A-Z]+_\\*\\*\\*):?(.*)");
  private static final Pattern TRAIL_MARK_PTN = Pattern.compile(" : *@?");
  private static final Pattern TRAIL_APT_PTN = Pattern.compile("#?(\\d+[A-Z]?|[A-Z])");
  private static final Pattern TRAIL_CALL_PTN = Pattern.compile("#[A-Z0-9]*\\d{3}_(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Anything starting with 3 asterisks is the similar but different (B) variant
    if (body.startsWith("***")) return false;
    
    // Drop anything that might be a E format
    if (subject.contains("FROM RELAY") || SUFFOLK_E_MARKER.matcher(body).lookingAt()) return false;

    // Some formats cut the initial TYPE: code
    if (body.startsWith("FWD:")) body = body.substring(4).trim();
    
    // Brentwood FD wraps their alert in some HTML text that needs to be stripped out
    if (body.startsWith("<HEAD>")) {
      int pt = body.indexOf("TYPE:");
      if (pt < 0) return false;
      body = body.substring(pt);
      pt = body.indexOf('\n');
      if (pt >= 0) body = body.substring(0,pt).trim();
    }
    boolean good = body.startsWith("TYPE:");
    if (!good) body = "TYPE:" + body;

    Properties props = parseMessage(body, KEYWORDS);
    
    data.strCall = props.getProperty("TYPE");
    if (data.strCall == null) return false;

    data.strCross = stripFieldEnd(props.getProperty("CROSS", ""), "/");;
    
    String sAddress = props.getProperty("LOC");
    if (sAddress == null) {
      if (data.strCross.length() > 0) {
        parseAddress(data.strCross, data);
        data.strCross = "";
      } else {
        if (!good) return false;
        Matcher match = CALL_ADDR_SPLIT_PTN.matcher(data.strCall);
        if (match.find()) {
          sAddress = data.strCall.substring(match.end());
          data.strCall = data.strCall.substring(0,match.start());
        } else {
          String sTmp = data.strCall;
          data.strCall = "";
          parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, sTmp, data);
        }
      }
    }
    if (sAddress != null) {
      
      if (sAddress.startsWith("/")) {
        data.strPlace = sAddress.substring(1).trim();
        parseAddress(data.strCross, data);
        data.strCross = "";
      }
      else {
        Matcher match = APT_PTN.matcher(sAddress);
        if (match.matches()) {
          sAddress = match.group(1).trim();
          data.strApt = match.group(2).trim();
        }
        
        match = ADDR_CROSS_PTN.matcher(sAddress);
        if (match.matches()) {
          sAddress = match.group(1).trim();
          data.strCross = append(match.group(2).trim(), " / ", data.strCross);
          parsePlaceField(getOptGroup(match.group(3)), data, false);
        }
        
        match = SPECIAL_PTN.matcher(sAddress);
        if (match.matches()) {
          sAddress = match.group(1).trim();
          data.strSupp = append(match.group(2).trim(), "\n", match.group(3).trim());
        }
  
        String sAddressPrefix = "";
        if (sAddress.startsWith("LL(")) {
          int pt = sAddress.indexOf(')', 3);
          if (pt >= 0) {
            sAddressPrefix = sAddress.substring(0,pt+1);
            sAddress = sAddress.substring(pt+1);
          }
        }
        String[] addrFlds = PLACE_MARK_PTN.split(sAddress, 3);
        if (addrFlds.length > 1) {
          sAddress = addrFlds[0].trim();
          parsePlaceField(addrFlds[1].trim(), data, true);
          if (addrFlds.length > 2) data.strSupp = append(data.strSupp, "\n", addrFlds[2].trim());
          match = APT_PTN.matcher(sAddress);
          if (match.matches()) {
            sAddress = match.group(1).trim();
            data.strApt = append(match.group(2).trim(), "-", data.strApt);
          }
        }
        sAddress = append(sAddressPrefix, " ", sAddress);
        
        // We have so many city codes that many of them form part of legitimate
        // street names, which really messes things up.  To cut down on some of
        // the confusion, any double blank following a legitimate city code is
        // treated as the end of the address
        int pt = -1;
        while (true) {
          pt = sAddress.indexOf("  ", pt+1);
          if (pt < 0) break;
          Result res = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, sAddress.substring(0,pt));
          if (res.getCity().length() > 0) {
            res.getData(data);
            data.strPlace = append(sAddress.substring(pt+2).trim(), " - ", data.strPlace);
            break;
          }
        }
        if (data.strCity.length() == 0) {
          parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS, sAddress, data);
          data.strPlace = append(getLeft(), " - ", data.strPlace);
        }
      }
    }
    
    data.strCode = props.getProperty("CODE", "");
    if (data.strCode.equals("default")) data.strCode = "";
    
    data.strCity = convertCodes(data.strCity, CITY_TABLE);
    String sTime = props.getProperty("TIME", "");
    Matcher match = TRAIL_MARK_PTN.matcher(sTime);
    if (match.find()) {
      parsePlaceField(sTime.substring(match.end()).trim(), data, false);
      sTime = sTime.substring(0,match.start()).trim();
    }
    if (sTime.length() > 5 && sTime.length() < 8) sTime = sTime.substring(0,5);
    if (sTime.length() >= 5) data.strTime = sTime;
    
    return true;
  }

  private void parsePlaceField(String field, Data data, boolean reverse) {
    Matcher match2;
    if ((match2 = TRAIL_APT_PTN.matcher(field)).matches()) {
      data.strApt = append(data.strApt, "-", match2.group(1), reverse);
    } else if ((match2 = TRAIL_CALL_PTN.matcher(field)).matches()) {
      data.strCall = append(data.strCall, " - ", match2.group(1));
    } else {
      data.strPlace = append(data.strPlace, " - ", field, reverse);
    }
  }
  
  private String append(String field1, String connect, String field2, boolean reverse) {
    if (reverse) {
      return append(field2, connect, field1);
    } else {
      return append(field1, connect, field2);
    }
  }

  static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "AMAGAN",  "AMAGANSETT",
      "AMITYV",  "AMITYVILLE",
      "AQUEBO",  "AQUEBOGUE",
      "ASHARO",  "ASHAROKEN",
      "ATLANT",  "ATLANTIQUE",
      "BABYLO",  "BABYLON",
      "BAITIH",  "BAITING HOLLOW",
      "BARRET",  "BARRET HOLLOW",
      "BAYPOR",  "BAYPORT",
      "BAYSHO",  "BAY SHORE",
      "BAYVIL",  "BAYVILLE",
      "BAYWOO",  "BAYWOOD",
      "BELLET",  "BELLE TERRE",
      "BELLMO",  "BELLMORE",
      "BELLPO",  "BELLPORT",
      "BETHPA",  "BETHPAGE",
      "BLUEPB",  "BLUE POINT BEACH",
      "BLUEPO",  "BLUE POINT",
      "BOHEMI",  "BOHEMIA",
      "BRENTW",  "BRENTWOOD",
      "BRIDGE",  "BRIDGEHAMPTON",
      "BRIGHT",  "BRIGHTWATERS",
      "BROOKH",  "BROOKHAVEN",
      "CALVER",  "CALVERTON",
      "CAPTRE",  "CAPTREE",
      "CENTEM",  "CENTER MORICHES",
      "CENTPO",  "CENTERPORT",
      "CENTRE",  "CENTEREACH",
      "CENTRI",  "CENTRAL ISLIP",
      "CHERRG",  "CHERRY GROVE",
      "COLDSH",  "COLD SPRING HARBOR",
      "COMMAC",  "COMMACK",
      "COPIAG",  "COPIAGUE",
      "CORAM",   "CORAM",
      "CORNEE",  "CORNEILLE ESTATES",
      "CUTCHO",  "CUTCHOGUE",
      "DAVISP",  "DAVIS PARK",
      "DEERPA",  "DEER PARK",
      "DERINH",  "DERING HARBOR",
      "DIXHIL",  "DIX HILLS",
      "DUNEWO",  "DUNEWOOD",
      "EASTPO",  "EASTPORT",
      "EATONN",  "EATONS  NECK",
      "EFARMI",  "EAST FARMINGDALE",
      "EFIREI",  "EAST FIRE ISLAND",
      "EHAMPT",  "EAST HAMPTON",
      "EHAMPV",  "EAST HAMPTION VILLAGE",
      "EISLIP",  "EAST ISLIP",
      "ELWOOD",  "ELWOOD",
      "EMARIO",  "EAST MARION",
      "EMORIC",  "EAST MORICHES",
      "ENORTH",  "EAST NORTHPORT",
      "EPATCH",  "EAST PATCHOGUE",
      "EQUOGU",  "EAST QUOGUE",
      "ESHORE",  "EAST SHOREHAM",
      "FAIRHA",  "FAIR HARBOR",
      "FARMDA",  "FARMINGDALE",
      "FARMVI",  "FARMINGVILLE",
      "FINS",    "FI NATIONAL SEASHORE",
      "FIREIP",  "FIRE ISLAND PINES",
      "FISHEI",  "FISHER ISLAND",
      "FLANDE",  "FLANDERS",
      "FORTSA",  "FORT SALONGA",
      "GARDII",  "GARDINERS ISLAND",
      "GILGOB",  "GILGO BEACH",
      "GORDOH",  "GORDON HEIGHTS",
      "GRANGI",  "GRAND GULL ISLAND",
      "GREATR",  "GREAT RIVER",
      "GREENL",  "GREENLAWN",
      "GREENP",  "GREENPORT",
      "GREENW",  "GREENPORT WEST",
      "HALESI",  "HALESITE",
      "HAMPTB",  "HAMPTON BAYS",
      "HAUPPA",  "HAUPPAUGE",
      "HEADHA",  "HEAD OF THE HARBOR",
      "HICKSV",  "HICKSVILLE",
      "HOLBRO",  "HOLBROOK",
      "HOLTSV",  "HOLTSVILLE",
      "HUNTIB",  "HUNTINGTON BAY",
      "HUNTIN",  "HUNTINGTON",
      "HUNTIS",  "HUNTINGTON STATION",
      "ISLAND",  "ISLANDIA",
      "ISLIP",   "ISLIP",
      "ISLIPT",  "ISLIP TERRACE",
      "JAMESP",  "JAMESPORT",
      "JONESB",  "JONES BEACH",
      "KINGPP",  "KINGS PARK PSYCHIATRIC",
      "KINGSP",  "KINGS PARK",
      "KISMET",  "KISMET",
      "LAKEGR",  "LAKE GROVE",
      "LAKERO",  "LAKE RONKONKOMA",
      "LAUREL",  "LAUREL HOLLOW",
      "LEVITT",  "LEVIGTTOWN",
      "LINDEN",  "LINDENHURST",
      "LITTGI",  "LITTLE GULL ISLAND",
      "LLOYDH",  "LLOYD HARBOR",
      "LONELY",  "LONELYVILLE",
      "MA",      "MALVERNE",
      "MANORV",  "MANORVILLE",
      "MASSAP",  "MASSAPEQUA",
      "MASSPA",  "MASSAPEQUA PARK",
      "MASTIB",  "MASTIC BEACH",
      "MASTIC",  "MASTIC",
      "MATTIT",  "MATTITUCK",
      "MEDFOR",  "MEDFORD",
      "MELVIL",  "MELVILLE",
      "MIDDLI",  "MIDDLE ISLAND",
      "MILLEP",  "MILLER PLACE",
      "MILLNE",  "MILL NECK",
      "MONTAU",  "MO NTAUK",
      "MORICH",  "MORICHES",
      "MOUNTS",  "MT SINAI",
      "MSPK",    "MATTITUCK",
      "NAMITY",  "NORTH AMITYVILLE",
      "NAPEAG",  "NAPEAGUE",
      "NBABYL",  "NORTH BABYLON",
      "NBAYSH",  "NORTH BAY SHORE",
      "NBELLP",  "NORTH BELLPORT",
      "NESCON",  "NESCONSET",
      "NEWSUF",  "NEW SUFFOLK",
      "NGREAR",  "NORTH GREAT RIVER",
      "NHAVEN",  "NORTH HAVEN",
      "NISSEQ",  "NISSEQUOGUE",
      "NLINDE",  "NORTH LINDENHURST",
      "NORTHP",  "NORTHPORT",
      "NORTHV",  "NORTHVILLE",
      "NORTVH",  "NORTHPORT VA",
      "NOYACK",  "NOYACK",
      "NPATCH",  "NORTH PATCHOGUE",
      "NSEA",    "NORTH SEA",
      "NWHARB",  "NORTH WEST HARBOR",
      "OAKBEA",  "OAK BEACH",
      "OAKDAL",  "OAKDALE",
      "OAKISL",  "OAK ISLAND",
      "OAKLEY",  "OAKLEYVILLE",
      "OCEABE",  "OCEAN BEACH",
      "OCEABP",  "OCEAN BAY PARK",
      "OCEANR",  "OCEAN RIDGE",
      "OLDBET",  "OLD BETHPAGE",
      "OLDFIE",  "OLD FIELD",
      "ORIENT",  "ORIENT",
      "OYSTERB",  "OYSTER BAY",
      "PATCHO",  "PATCHOGUE",
      "PECONI",  "PECONIC",
      "PILGRP",  "PILGRIM PSYCHIATRIC",
      "PLAINV",  "PLAINVIEW",
      "PLUMIS",  "PLUM ISLAND",
      "POINTO",  "POINT O’WOODS",
      "POOSIR",  "POOSPATUCK INDIAN RESERVATION",
      "POQUOT",  "POQUOTT",
      "PORTJE",  "PORT JEFFERSON",
      "PORTJS",  "PORT JEFFERSON STATION",
      "QUIOGU",  "QUIOGUE",
      "QUOGUE",  "QUOGUE",
      "REMSEN",  "REMSENBURG",  
      "REMSES",  "REMSENBURG-SPEONK",
      "RIDGE",   "RIDGE",
      "RIVERH",  "RIVERHEAD",
      "RIVERS",  "RIVERSIDE",
      "ROBBII",  "ROBBINS ISLAND",
      "ROBBIR",  "ROBBINS REST",
      "ROBERM",  "ROBERT MOSES PARK",
      "ROCKYP",  "ROCKY POINT",
      "RONKON",  "RONKONKOMA",
      "SAGAPO",  "SAGAPONACK",
      "SAHGAR",  "SAG HARBOR",
      "SAILOH",  "SAILORS HAVEN",
      "SAINTJ",  "ST JAMES",
      "SALTAI",  "SALTAIRE",
      "SAYVIL",  "SAYVILLE",
      "SEAFOR",  "SEAFORD",
      "SEAVIE",  "SEAVIEW",
      "SELDEN",  "SELDEN",
      "SETAES",  "SETAKET-EAST SETAUKET",
      "SHELIH",  "SHELTHER ISLAND HEIGHTS",
      "SHELTI",  "SHELTER ISLAND",
      "SHINIR",  "SHINNECOCK INDIAN RESERVATION",
      "SHINNH",  "SHINNECOCK HILLS",
      "SHIRLE",  "SHIRLEY",
      "SHOREH",  "SHOREHAM",
      "SHUNTI",  "SOUTH HUNTINGTON",
      "SMITHP",  "SMITH POINT",
      "SMITHT",  "SMITHTOWN",
      "SOUNDB",  "SOUND BEACH",
      "SOUTHA",  "SOUTHAMPTON",
      "SOUTHO",  "SOUTHOLD",
      "SOUTHV",  "SOUTHAMPTON VILLAGE",
      "SPEONK",  "SPEONK",
      "SPRING",  "SPRINGS",
      "STONYB",  "STONY BROOK",
      "SUFFDC",  "SUFFOLK DEVELOPMENTAL CENTER",
      "SUNKEF",  "SUNKEN FOREST",
      "SUNYFA",  "SUNY FARMINGDALE",
      "SUNYSB",  "SUNY STONY BROOK",
      "SYOSSE",  "SYOSSET",
      "TALISM",  "TALISMAN",
      "TERRYV",  "TERRYVILLE",
      "TOBAY",   "TOBAY",
      "UPTON",   "UPTON",
      "USCGST",  "US COAST GUARD STATION",
      "VILLAB",  "VILLAGE OF THE BRANCH",
      "WADINR",  "WADING RIVER",
      "WAINSC",  "WAINSCOTT",
      "WANTAG",  "WANTAGH",
      "WATCHH",  "WATCH HILL",
      "WATERI",  "WATER ISLAND",
      "WATERM",  "WATER MILL",
      "WBABYL",  "WEST BABYLON",
      "WBAYSH",  "WEST BAY SHORE",
      "WFIREI",  "WEST FIRE ISLAND",
      "WHAMPB",  "WESTHAMPTON BEACH",
      "WHAMPD",  "WESTHAMPTON DUNES",
      "WHAMPT",  "WESTHAMPTON",
      "WHEATH",  "WHEATLEY HEIGHTS",
      "WHILLS",  "WEST HILLS",
      "WISLIP",  "WEST ISLIP",
      "WOODBU",  "WOODBURY",
      "WSAYVI",  "WEST SAYVILLE",
      "WYANDA",  "WYANDANCH",
      "YAPHAN",  "YAPHANK",
      
      "E NORTHPORT",    "E NORTHPORT",
      "SETAUKET",       "SETAUKET",
      "SO FARMINGDALE", "SOUTH FARMINGDALE"

  });
}
