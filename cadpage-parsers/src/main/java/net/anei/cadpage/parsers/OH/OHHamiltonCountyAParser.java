package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.ReverseCodeTable;


public class OHHamiltonCountyAParser extends FieldProgramParser {
 
  public OHHamiltonCountyAParser() {
    super(CITY_CODES, "HAMILTON COUNTY", "OH", 
          "CALL! ( SELECT/2 CH ADDR! Bld:APT! Apt:APT! PLACE SRC TIME UNIT NAME! " +
                "| Bld:APT! Apt:APT/D! ADDR PLACE SRC TIME UNIT CH Alarm:PRI! ) Xst:X! END");
    setupDoctorNames("FELDMAN", "KATIE");
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupDoctorNames(
        "MATT",
        "MAYO",
        "MESSTER",
        "RISCALLA",
        "WILSON"
    );
    setupSaintNames(
        "CLAIRE",
        "JOHN"
    );
    removeWords("ALLEY", "MALL");
    setupCallList(CALL_LIST);
    addRoadSuffixTerms("CONNECTOR");
  }
  
  @Override
  public String getFilter() {
    return "hc@hamilton-co.org,9300,messaging@iamresponding.com,6245";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return WILLIAM_HENRY_HARR.matcher(addr).replaceAll("WILLIAM HENRY HARRISON");
  }
  private static final Pattern WILLIAM_HENRY_HARR = Pattern.compile("\\bWILLIAM HENRY HARR\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Pattern PREFIX_PTN = Pattern.compile("(?:HC|CAD|DIRECT):");
//  private static final Pattern MASTER3 = Pattern.compile("CAD:(.*?) Bld: *(.*?) Apt: *(.*) ([A-Z][A-Z0-9]):([A-Z][A-Z0-9,]*) (?:(.*) )?Xst:(.*)");
  private static final Pattern CALL_CH_ADDR_PTN = Pattern.compile("(.*?) \\*?((?:AM|FG)\\d{1,2})=?(.*)");
  
  
  private static final Pattern MASTER2 = Pattern.compile("CAD:(.*?) Bld: *(.*?) \\\\?Apt: *(.*?) *(\\d\\d:\\d\\d) *([A-Z][A-Z0-9,]*) (?:(\\d[A-Z]\\d\\d) )?(?:(.*))?Xst:(.*)");
  private static final Pattern CALL_PFX_PTN = Pattern.compile("(2ND CALL) *(.*)");
  
  private static final Pattern MASTER1 = Pattern.compile("HC:(.*?)(?: \\*\\*? (.*?) \\*\\*( .*?)??)?(?: (\\d{1,2}:\\d\\d)( .*)?)?");
  private static final Pattern APT_PTN = Pattern.compile("(.*?) +APT: *([^ ]+) +(.*)");
  private static final Pattern ORIG_LOC_PTN = Pattern.compile(" *\\bOriginal Location : *");
  private static final Pattern YMCA_PTN = Pattern.compile("\\bY ?M ?C ?A\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String body, Data data) {
    
    if (body.startsWith("HC:AD:") || body.startsWith("HC:TIME:")) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body.substring(3);
      return true;
    }
    
    // There are two different field delimited formats.
    // One uses pipe delimiters, the other users either \ or > delimiters
    String[] flds = body.split("\\|");
    if (flds.length >= 5) {
      setSelectValue("1");
    } else {
      flds = body.split(">");
      if (flds.length < 5) flds = body.split("\\\\");
      if (flds.length >= 5) setSelectValue("2");
      else flds = null;
    }
    if (flds != null) {
      Matcher match = PREFIX_PTN.matcher(flds[0]);
      if (!match.lookingAt()) return false;
      flds[0] = flds[0].substring(match.end()).trim();
      
      String prefix = "";
      match = CALL_PFX_PTN.matcher(flds[0]);
      if (match.matches()) {
        prefix = match.group(1);
        flds[0] = match.group(2);
      }
      if (!parseFields(flds, data)) return false;
      data.strCall = append(prefix, " ", data.strCall);
      
      String city = DEPT_CITY_TABLE.getCodeDescription(data.strSource);
      if (city != null) data.strCity = city;
      return true;
    }
    
    // Otherwise we have to do this the hard way :(
    else {
      Matcher match;
//      if ((match = MASTER3.matcher(body)).matches()) {
//        setFieldList("CALL CH ADDR APT PLACE SRC CITY TIME UNIT MAP NAME X");
//        String callAddr = match.group(1).trim();
//        String apt = match.group(2).trim();
//        String aptCity = match.group(3).trim();
//        data.strSource = match.group(4);
//        data.strUnit = match.group(5);
//        data.strName = getOptGroup(match.group(6));
//        data.strCross = match.group(7).trim();
//
//        // Strip off possibly call prefix
//        String prefix = "";
//        match = CALL_PFX_PTN.matcher(callAddr);
//        if (match.matches()) {
//          prefix = match.group(1);
//          callAddr = match.group(2);
//        }
//        
//        // If we are lucky, there is a channel separating the call and address
//        // Otherwise use the SAP to split them
//        match = CALL_CH_ADDR_PTN.matcher(callAddr);
//        if (match.matches()) {
//          data.strCall = match.group(1).trim();
//          data.strChannel = match.group(2);
//          parseAddress(match.group(3).trim(), data);
//        } else {
//          parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_CITY | FLAG_ANCHOR_END, callAddr, data);
//        }
//        data.strCall = append(prefix, " ", data.strCall);
//        data.strApt = append(data.strApt, "-", apt);
//        
//        // Split place name from  source
//        ReverseCodeTable.Result res = DEPT_CITY_TABLE.getResult(placeSource);
//        if (res == null) return false;
//        data.strSource = res.getCode();
//        data.strCity = res.getDescription();
//        data.strPlace = res.getRemainder();
//        
//        return true;
//      }
      
      if ((match = MASTER2.matcher(body)).matches()) {
        setFieldList("CALL CH ADDR APT PLACE SRC CITY TIME UNIT MAP NAME X");
        String callAddr = match.group(1).trim();
        String apt = match.group(2).trim();
        String placeSource = match.group(3).trim();
        data.strTime = match.group(4);
        data.strUnit = match.group(5);
        data.strMap = getOptGroup(match.group(6));
        data.strName = getOptGroup(match.group(7));
        data.strCross = match.group(8).trim();

        // Strip off possibly call prefix
        String prefix = "";
        match = CALL_PFX_PTN.matcher(callAddr);
        if (match.matches()) {
          prefix = match.group(1);
          callAddr = match.group(2);
        }
        
        // If we are lucky, there is a channel separating the call and address
        // Otherwise use the SAP to split them
        match = CALL_CH_ADDR_PTN.matcher(callAddr);
        if (match.matches()) {
          data.strCall = match.group(1).trim();
          data.strChannel = match.group(2);
          parseAddress(match.group(3).trim(), data);
        } else {
          parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_CITY | FLAG_ANCHOR_END, callAddr, data);
        }
        data.strCall = append(prefix, " ", data.strCall);
        data.strApt = append(data.strApt, "-", apt);
        
        // Split place name from  source
        ReverseCodeTable.Result res = DEPT_CITY_TABLE.getResult(placeSource);
        if (res == null) return false;
        data.strSource = res.getCode();
        data.strCity = res.getDescription();
        data.strPlace = res.getRemainder();
        
        return true;
      }
      
      else if ((match = MASTER1.matcher(body)).matches()) {
        setFieldList("ADDR CITY ST APT PLACE CALL INFO TIME UNIT X");
        
        String addr = match.group(1).trim();
        String call = match.group(2);
        String info = match.group(3);
        String time = match.group(4);
        String extra = match.group(5);
        
        // Clean blanks out of YMCA RD
        addr = YMCA_PTN.matcher(addr).replaceAll("YMCA");
        
        // Old format had an asterisk delimited call field with a place
        // name in front of it and a info field behind.  There is another
        // call description in front of the address that duplicates the
        // asterisk delimited field so we just skip it
        if (call != null) {
          parseAddress(StartType.START_OTHER, FLAG_IGNORE_AT | FLAG_CROSS_FOLLOWS, addr, data);
          String sPlace = fixCity(getLeft(), data);
          if (sPlace.startsWith("APT ")) {
            Parser p = new Parser(sPlace.substring(4).trim());
            data.strApt = p.get(' ');
            sPlace = p.get();
          }
          data.strPlace = sPlace;
          data.strCall = call.trim();
          data.strSupp = getOptGroup(info);
          if (data.strSupp.startsWith("Original Location :")) data.strSupp = "";
        }
        
        // New format just has one field with a call description, address, and additional information
        else {
          
          // Sometimes the put an APT: label and field between the call and address
          // which makes things easier
          StartType st = StartType.START_CALL;
          int flags = FLAG_START_FLD_REQ;
          match = APT_PTN.matcher(addr);
          if (match.matches()) {
            st = StartType.START_ADDR;
            flags = 0;
            data.strCall = match.group(1);
            data.strApt = match.group(2);
            addr = match.group(3);
          }
          
          // otherwise we have to do this the hard way.
          
          parseAddress(st, flags | FLAG_IGNORE_AT | FLAG_CROSS_FOLLOWS, addr, data);
          if (data.strAddress.length() == 0) {
            data.msgType = MsgType.GEN_ALERT;
            data.strSupp = append(data.strCall, " - ", getLeft());
            data.strCall = "";
            return true;
          }
          info = fixCity(getLeft(), data);
          if (info.startsWith("LOC:")) {
            info = info.substring(4).trim();
            match = ORIG_LOC_PTN.matcher(info);
            if (match.find()) info = info.substring(0,match.start());
            data.strPlace = info;
          } else {
            match = ORIG_LOC_PTN.matcher(info);
            if (match.find()) {
              String place = info.substring(match.end());
              if (!data.strAddress.contains(place)) data.strPlace = place;
              info = info.substring(0,match.start());
            }
            data.strSupp = info;
          }
        }
    
        // Fix some Dispatch address typos
        data.strAddress = data.strAddress.replace("DE SOTO", "DESOTO");
        
        // Process time if present
        if (time  != null) data.strTime = time;
    
        // Process leftover stuff
        if (extra != null) {
          Parser p = new Parser(extra.trim());
          String x2 = p.getLastOptional(" XST2:");
          String x1 = p.getLastOptional(" XST:");
          data.strUnit = p.get();
          data.strCross = append(x1, " & ", x2);
        }
        return true;
      }
    }
    
    return false;
  }
  
  private static String fixCity(String left, Data data) {
    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      String state = data.strCity.substring(pt+1);
      data.strCity = data.strCity.substring(0,pt);
      if (!state.equals("OH")) data.strState = state;
      Matcher match = COUNTY_PTN.matcher(left);
      if (match.matches()) left = match.group(1);
    }
    return left;
  }
  private static final Pattern COUNTY_PTN = Pattern.compile("[A-Z]+ +(?:CO|CTY)\\b *(.*)");
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("SRC", "SRC CITY");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new ChannelField("[A-Z]{2}\\d{2}|", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ALTA VISTA",
    "ANDERSON COVE",
    "ANDERSON FERRY",
    "ANDERSON GLEN",
    "APPLE FARM",
    "APPLE HILL",
    "APPLE ORCHARD",
    "ASBURY HILLS",
    "ASHLEY OAKS",
    "ASHLEY VIEW",
    "ASPEN POINT",
    "ASPEN VIEW",
    "ASTON LAKE",
    "ASTON OAKS",
    "ASTON WOODS",
    "AVON FIELDS",
    "BAYLEY PLACE",
    "BEACON HILL",
    "BEECH GROVE",
    "BENTON RIDGE",
    "BERKSHIRE CLUB",
    "BLUE ASH",
    "BLUE CUT",
    "BLUE LAKE",
    "BLUE ROCK",
    "BLUEJAY VIEW",
    "BRAMBLE HILL",
    "BRANCH HILL GUINEA",
    "BRANCH HILL",
    "BREEZY VISTA",
    "BRIDGE POINT",
    "BRIERLY CREEK",
    "BRIGHT WATER",
    "BUENA VISTA",
    "BUFFALO RIDGE",
    "BUNKER HILL",
    "BURNING TREE",
    "CAROLINA TRACE",
    "CARROUSEL PARK",
    "CARRY BACK",
    "CARVER WOODS",
    "CAULY WOODS",
    "CEDAR POINT",
    "CENTER HILL",
    "CHAPEL HEIGHTS",
    "CHASE PLAZA",
    "CHERRY BLOSSOM",
    "CHESTNUT PARK",
    "CHESTNUT RIDGE",
    "CHUCK HARMON",
    "CINCINNATI MILLS",
    "CLEVES WARSAW",
    "COMPTON LAKE",
    "COMPTON RIDGE",
    "CONCORD WOODS",
    "CONGRESS GREEN",
    "CONGRESS RUN",
    "COOKS CROSSING",
    "CORNELL PARK",
    "COUNT FLEET",
    "COUNTRY TRACE",
    "COUNTRY VIEW",
    "COUNTRY VILLAGE",
    "COUNTRY WALK",
    "COUNTRY WOODS",
    "COVERED BRIDGE",
    "CREST RIDGE",
    "CROSSING AT PARK",
    "CROWNE POINT",
    "CRYSTAL VIEW",
    "CURLY MAPLE",
    "DARK STAR",
    "DE ARMAND",
    "DE SOTO",
    "DEER CHASE",
    "DEER PARK",
    "DEERVIEW PARK",
    "DEERVIEW WOODS",
    "DELHI PARK",
    "DELLERS GLEN",
    "DOG TROT",
    "DREWRY FARM",
    "DRY FORK",
    "DRY RIDGE",
    "DRY RUN",
    "DUCK CREEK",
    "DUGAN GAP",
    "DURANGO GREEN",
    "EAGLE RIDGE",
    "EAGLES LAKE",
    "EAST MIAMI RIVER",
    "EAST WILLOW",
    "EDEN PLACE",
    "EDEN RIDGE",
    "EIGHT MILE",
    "EMERALD GLADE",
    "EMERALD LAKES",
    "ENGLAND CLUB",
    "ENTERPRISE PARK",
    "ESTER MARIE",
    "FAIR OAKS",
    "FARM ACRES",
    "FAWN MEADOW",
    "FIDDLERS GREEN",
    "FIELDS ERTEL",
    "FIVE MILE",
    "FOREST HILL",
    "FOREST VIEW",
    "FORFEIT RUN",
    "FORT SCOTT",
    "FOUR MILE",
    "FOUR WORLDS",
    "FOX CHASE",
    "FOX CUB",
    "FOX RIDGE",
    "FOX RUN",
    "GALBRAITH POINTE",
    "GALLANT FOX",
    "GEORGE LEWIS",
    "GLEN EAGLE",
    "GLEN MEADOW",
    "GLEN OAKS",
    "GLENDALE MILFORD",
    "GOOD SAMARITAN",
    "GORMAN HERITAGE FARM",
    "GOVERNORS HILL",
    "GRAND OAKS",
    "GREEN ACRES",
    "GUM RUN",
    "HAMILTON CLEVES",
    "HAPPY HOLLOW",
    "HARBOR RIDGE",
    "HARRISON BROOKVILLE",
    "HARRY LEE",
    "HARVARD ACRES",
    "HERITAGE LAKE",
    "HERITAGE SQUARE",
    "HICKORY FLATS",
    "HICKORY HILL",
    "HICKORY POINT",
    "HICKORY TRAIL",
    "HICKORY VIEW",
    "HIDDEN MEADOWS",
    "HIDDEN VALLEY",
    "HIGH POINTE",
    "HIGH TREE",
    "HOFFMAN FARM",
    "HOLIDAY HILLS",
    "HOLMAN VIEW",
    "HUNTERS CREEK",
    "INDIAN CREEK",
    "INDIAN HILL",
    "INDIAN RIDGE",
    "INDIAN TRACE",
    "INDIAN VIEW",
    "INDIAN WOODS",
    "IRON BRIDGE",
    "IRON WOODS",
    "IVY HILLS",
    "JAKE SWEENEY",
    "JAMISON CREEK",
    "JOHN GRAY",
    "JORDAN CLUB",
    "JORDAN RIDGE",
    "JORDAN WOODS",
    "KATIES GREEN",
    "KEMPER COMMONS",
    "KEMPER MEADOW",
    "KEMPER WOODS",
    "KEN ARBRE",
    "KENWOOD CROSSING",
    "KUGLER MILL",
    "KULIGA PARK",
    "LA BOITEAUX",
    "LAKE FOREST",
    "LAKE VIEW",
    "LANGDON FARM",
    "LAUREL RIDGE",
    "LE MAR",
    "LE ROY",
    "LEES CREEK",
    "LEES CROSSING",
    "LEGENDARY RIDGE",
    "LITTLE CREEK",
    "LITTLE DRY RUN",
    "LITTLE DRY",
    "LOCUST RIDGE",
    "LOGAN CREEK",
    "LORD ALFRED",
    "LYTLE WOODS",
    "MADEIRA HILLS",
    "MALLARD COVE",
    "MALLARD CROSSING",
    "MAPLE KNOLL TERRACE",
    "MAPLE VIEW",
    "MAR DEL",
    "MAR RIC",
    "MARKET PLACE",
    "MASON MONTGOMERY",
    "MAYVIEW FOREST",
    "MEADOW BROOK",
    "MEADOW CREEK",
    "MEADOW GROVE",
    "MEADOW LAKE",
    "MEADOW LARK",
    "MEADOW VALLEY",
    "MERCY HEALTH",
    "MERRITT GROVE",
    "MIAMI BLUFF",
    "MIAMI LAKES",
    "MIAMI RIVER",
    "MIAMI WOODS",
    "MICHAEL ANTHONY",
    "MICHELLES OAK",
    "MILES WOODS",
    "MILFORD VISTA",
    "MILLVILLE SHANDON",
    "MISTY MEADOW",
    "MITCHELL FARM",
    "MITCHELL PARK",
    "MITCHELL WAY",
    "MONFORT HEIGHTS",
    "MORGAN ROSS",
    "MORNING GLORY",
    "MT ALVERNO",
    "MT CARMEL",
    "MT HOPE",
    "MT NEBO",
    "MT PLEASANT",
    "MT VERNON",
    "MUDDY CREEK",
    "MYSTICAL ROSE",
    "NORTH BAY",
    "NORTH BEND",
    "NORTH COMMERCE PARK",
    "NORTH GLEN",
    "NORTH HILL",
    "OAK BROOK",
    "OAK KNOLL",
    "OAK POST",
    "OAKWOOD PARK",
    "OLDE GATE",
    "ORCHARD CLUB",
    "ORCHARD HILL",
    "ORCHARD VIEW",
    "PADDYS RUN",
    "PARK 42",
    "PEBBLE BROOKE",
    "PINE MEADOW",
    "PIPPIN MEADOWS",
    "PLEASANT RUN",
    "POAGE FARM",
    "POWNER FARM",
    "QUAIL HOLLOW",
    "QUARTER MAINE",
    "RAINBOW HILL",
    "RAPID RUN",
    "RAY NORRISH",
    "RED BANK",
    "RED BIRD",
    "RED BUD",
    "RED FOX",
    "REED HARTMAN",
    "REGENCY RIDGE",
    "REGENCY RUN",
    "RIO GRANDE",
    "RIVER CREST",
    "RIVER WALK",
    "RIVERS EDGE",
    "RMP MONTGOMERY",
    "ROCK MAPLE",
    "ROLLING HILLS",
    "RONALD REAGAN",
    "ROSE HILL",
    "ROSE PETAL",
    "ROUND BOTTOM",
    "ROUND TOP",
    "ROWAN HILL",
    "ROYAL GLEN",
    "ROYAL OAK",
    "RUWES OAK",
    "SAINT JOE",
    "SAMOHT RIDGE",
    "SAND RUN",
    "SCHOOL SECTION",
    "SHAKER POINT",
    "SHARON PARK",
    "SHAWNEE PINES",
    "SHAWNEE RUN",
    "SHEPHERD CREEK",
    "SHORE VIEW",
    "SIGNAL HILL",
    "SIGNAL POINTE",
    "SILVER MAPLE",
    "SOCIALVILLE FOSTER",
    "SOUTH EAGLESNEST",
    "SOUTH MILFORD",
    "SOUTH SHORE",
    "SOUTH TERRACE",
    "SOUTH WHETZEL",
    "SPDL BLUE ROCK",
    "SPOOKY HOLLOW",
    "SPOOKY RIDGE",
    "SPRING GROVE",
    "SPRING HILL",
    "SPRINGDALE LAKE",
    "SPY GLASS",
    "SQUIRREL RIDGE",
    "ST ALBANS",
    "ST ANDREWS",
    "ST ANN",
    "ST CLAIR",
    "ST CLAIRE",
    "ST CLOUD",
    "ST DOMINIC",
    "ST EDMUNDS",
    "ST JAMES",
    "ST JOHNS",
    "ST MARTINS",
    "ST REGIS",
    "STATE LINE",
    "STONE BARN",
    "STONE CREEK",
    "STONE RIDGE",
    "STONEY CULVERT",
    "SUGAR CAMP",
    "SUMMIT LAKE",
    "SUSPENSION BRIDGE",
    "SUTTERS MILL",
    "SYCAMORE GROVE",
    "TALL OAKS",
    "TALL PINES",
    "TANGLEWOOD PARK",
    "THREE RIVERS",
    "TIM TAM",
    "TIMBER RIDGE",
    "TIMBER TOP",
    "TIMBER WAY",
    "TOMY LEE",
    "TOWERING RIDGE",
    "TOWNE CENTER",
    "TOWNE COMMONS",
    "TREE HEIGHTS",
    "TRI COUNTY",
    "TRI RIDGE",
    "TRIANGLE PARK",
    "TURF WOOD",
    "TWELVE OAKS",
    "TWO MILE",
    "UNION CENTRE",
    "VALLEY JUNCTION",
    "VALLEY RIDGE",
    "VALLEY VIEW",
    "VAN BUREN",
    "VAN FLEET",
    "VAN NES",
    "VAN ROBERTS",
    "VAN ZANDT",
    "VICTOR STIER",
    "VIEW PLACE",
    "VIEW POINTE",
    "VILLAGE GATE",
    "VILLAGE GREEN",
    "VINTAGE CLUB",
    "VISTA VIEW",
    "WAGON ROAD",
    "WALDEN GLEN",
    "WALLACE GROVE",
    "WALNUT WOODS",
    "WALTON CREEK",
    "WAR ADMIRAL",
    "WATCH POINT",
    "WAYNE PARK",
    "WEST FORDHAM",
    "WEST FORK",
    "WEST HILL",
    "WEST MC KELVEY",
    "WESTIN RIDGE",
    "WESTWOOD NORTHERN",
    "WHISPERING FARM",
    "WHITE WATER",
    "WHITES HILL",
    "WILLIAMS CREEK",
    "WILLOW HILLS",
    "WILLOW RUN",
    "WINDING BROOK",
    "WINDY HILLS",
    "WINTON HILLS",
    "WINTON WOODS",
    "WITTS MILL",
    "WOLFPEN PLEASANT HILL",
    "WOOD BLUFF",
    "WOODLAND RESERVE",
    "WOODLANDS PATH",
    "WOODLANDS RIDGE",
    "ZIG ZAG",
    "ZION HILL"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ADDY", "ADDYSTON",
      "AMBR", "AMBERLY VILLAGE",
      "ANDR", "ANDERSON TWP",
      "ARLN", "ARLINGTON HEIGHTS",
      "BLUE", "BLUE ASH",
      "CHEV", "CHEVIOT",
      "CINC", "CINCINNATI",
      "CLVS", "CLEVES",
      "COTP", "COLERAIN TWP",
      "COLM", "COLUMBIA TWP",
      "CROS", "CROSBY TWP",
      "DLHI", "DELHI TWP",
      "DRPK", "DEER PARK",
      "ELMW", "ELMWOOD PLACE",
      "EVEN", "EVENDALE",
      "FRFD", "FAIRFIELD",
      "FRFX", "FAIRFAX",
      "FRPK", "FOREST PARK",
      "GLEN", "GLENDALE",
      "GOLF", "GOLF MANOR",
      "GREN", "GREENHILLS",
      "GRTP", "GREEN TWP",
      "HARR", "HARRISON",
      "HRTP", "HARRISON TWP",
      "INDN", "INDIAN HILL",
      "LINC", "LINCOLN HEIGHTS",
      "LOCK", "LOCKLAND",
      "LOVE", "LOVELAND",
      "MDRA", "MADEIRA",
      "MRMT", "MARIEMONT",
      "MIAC", "MIAMI TWP",
      "MIAM", "MIAMI TWP",
      "MILF", "MILFORD",
      "MONT", "MONTGOMERY",
      "MRMT", "MARIEMONT",
      "MTHL", "MOUNT HEALTHY",
      "NCHL", "NORTH COLLEGE HILL",
      "NEWT", "NEWTOWN",
      "NORW", "NORWOOD",
      "NRBN", "NORTH BEND",
      "NCHL", "NORTH COLLEGE HILL",
      "READ", "READING",
      "SHRN", "SHARONVILLE",
      "SLVT", "SILVERTON",
      "SPDL", "SPRINGDALE",
      "SPTP", "SPRINGFIELD TWP",
      "STBN", "SAINT BERNARD",
      "SYCM", "SYCAMORE TWP",
      "SYMM", "SYMMES TWP",
      "TERR", "TERRACE PARK",
      "UNTC", "UNION TWP",
      "WWTR", "WHITEWATER TWP",
      "WOOD", "WOODLAWN",
      "WWTR", "WHITEWATER TWP",
      "WYOM", "WYOMING",
      
      // Butler County
      "MGTP", "MORGAN TWP",
      "WCHE", "WEST CHESTER TWP",

      // Clermont County
      "CLER", "CLERMONT COUNTY",

      // Dearborn County, IN
      "DEAR", "DEARBORN COUNTY/IN",
      "HRTI", "HARRISON TWP/IN",
      "WHAR", "WEST HARRISON/IN",
      
      // Warren County
      "DFTP", "DEERFIELD TWP/OH",
      
      "FRANKLIN CO",   "FRANKLIN COUNTY",
      "JACKSON BUTL",  "HAMILTON"
      
  });
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "A/A-BUILDING STRUCK",
      "A/A-FIRE/FUEL LEAK",
      "A/A -",
      "A/A -BUILDING STRUCK",
      "A/A - ENTRAPMENT",
      "A/A - FUEL LEAK/FIRE",
      "A/A - INJURY",
      "A/A - PERSON STRUCK",
      "A/A-ENTRAPMENT",
      "A/A-INJURY",
      "A/A-PEDESTRIAN STRUCK",
      "ABDOMINAL PAIN",
      "ALLERGIC REACTION",
      "ANIMAL BITE",
      "APPLIANCE FIRE",
      "ASSAULT-INJURED",
      "ATTEMPT/THREAT SUICIDE",
      "BACK PAIN",
      "BOMB THREAT/DEVICE",
      "BRUSH/MULCH/FIELD FIRE",
      "BURNED PERSON",
      "CHECK TAZED PERSON",
      "CHEMICAL BURN",
      "CHEMICAL SPILL",
      "CHEST PAIN",
      "CHIMNEY FIRE",
      "CHOKING",
      "CO ALARM",
      "CO ALARM - SICK PERS",
      "CO ALARM-ILLNESS",
      "DIABETIC EMERGENCY",
      "DROWNING",
      "DUMPSTER FIRE",
      "ELECTRICAL FIRE",
      "ELECTRICAL INJURY",
      "ELEVATOR ALARM/RESCU",
      "ELEVATOR ALARM/RESCUE",
      "EMERGENCY TO PROPERTY",
      "EMS / LIFT ASSISTANCE",
      "EMS LIFT ASSIST",
      "EYE INJURY",
      "EXPOSURE TO HAZMAT",
      "FIELD/BRUSH/MULCH",
      "FIRE ALARM",
      "FUEL SPILL",
      "GENERAL ALERT",
      "GENERAL RESPONSE",
      "GYNECOLOGICAL EMER",
      "GYNECOLOGICAL EMERGENCY",
      "HAZMAT INCIDENT",
      "HEADACHE",
      "HEAD INJURY",
      "HEART ATTACK",
      "HEMORRHAGING",
      "HIGH ANGLE RESCUE",
      "HIGH FEVER",
      "HYPERTHERMIA",
      "HYPOTHERMIA",
      "INDUSTRIAL ACCIDENT",
      "INJ DURING ROBBERY",
      "INJURED PERSON",
      "INJURY FROM A FALL",
      "INVESTIGATION",
      "LACERATION",
      "LIFT ASSISTANCE",
      "MATERNITY RUN",
      "MEDICAL ALARM",
      "MISCARRIAGE",
      "NON-BREATHER",
      "NON-BREATHER/CARDIAC ARRE",
      "ODOR OF GAS",
      "ODOR OF NATURAL GAS",
      "ODOR/LGT SMOKE INDOO",
      "ODOR/SMOKE OUTDOORS",
      "OPEN BURN",
      "OVERDOSE",
      "PERSON ASSAULTED",
      "PERSON BURNED",
      "PERSON ELECTROCUTED",
      "PERSON HEMORRHAGING",
      "PERSON IN SEIZURES",
      "PERSON INJURED",
      "PERSON INJURED IN A FALL",
      "PERSON SHOT",
      "PERSON SICK",
      "PERSON STABBED",
      "PERSON UNCONSCIOUS / NON-RESPONSIVE",
      "PERSON WITH A HEAD INJURY",
      "POISONING",
      "POLE/TRANSFORM FIRE",
      "POLE/TRANSFORMER FIRE",
      "POSSIBLE HEART ATTACK",
      "PROPERTY EMERGENCY",
      "PSYCHIATRIC EMER",
      "PSYCHIATRIC EMERGENCY",
      "REC ELEVATOR ALARM",
      "RECORDED ELEVATOR ALARM",
      "RECORDED FIRE ALARM",
      "ROBBERY-INJURY",
      "SEIZURES",
      "SET UP L/Z",
      "SEXUAL ASSAULT",
      "SEXUAL ASSAULT-INJURED",
      "SHOOTING",
      "SICKLE CELL EMER",
      "SICKLE CELL EMERGENCY",
      "SICK PERSON",
      "SMELL OF GAS",
      "SMOKE/ODOR INDOORS",
      "SMOKE/ODOR OUTDOORS",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDE ATT/THREATS",
      "TEST FIRE DETAIL",
      "TESTING",
      "TROUBLE BREATHING",
      "UNCONSCIOUS",
      "UNCONSCIOUS PERSON",
      "VEHICLE FIRE",
      "WATER FLOW ALARM",
      "WATER RESCUE/RECOVER",
      "WIRES DOWN",
      "WIRES DOWN/ARCING/FIRE"
  );
  
  private static final ReverseCodeTable DEPT_CITY_TABLE = new ReverseCodeTable(
      "Amberly Village FD",         "Amberley Village",
      "Amberley Villag",            "Amberley Village",
      "Anderson Twp FD",            "Cincinnati",
      "Blue Ash FD",                "Blue Ash",
      "Cheviot FD",                 "Cheviot",
      "Cincinnati FD",              "Cincinnati",
      "Clermont CO FD",             "Clermont County",
      "Cleves FD",                  "Cleves",
      "Colerain Twp FD",            "Colerain Twp",
      "Crosby Twp FD",              "Crosby Twp",
      "Deerpark/Silverton FD",      "Deer Park",
      "Deerpark-Silver",            "Deer Park",
      "Delhi Twp FD",               "Delhi Twp",
      "Elmwood F",                  "Elmwood",
      "Elmwood FD",                 "Elmwood",
      "Elmwood Place F",            "Elmwood Place",
      "Evendale FD",                "Evendale",
      "Fairfield FD",               "Fairfield",
      "Forest Park FD",             "Forest Park",
      "Glendale FD",                "Glendale",
      "Golf Manor FD",              "Golf Manor",
      "Green Twp FD",               "Green Twp",
      "Greenhills FD",              "Greenhills",
      "Harrison FD",                "Harrison Twp",
      "Lincoln Heights",            "Lincoln Heights",
      "Lincoln Heights FD",         "Lincoln Heights",
      "Little Miami JFD",           "Fairfax",
      "Little Miami JF",            "Fairfax",
      "Lockland FD",                "Lockland",
      "Madeira/Indian Hill JFD",    "Madeira",
      "Madeira-Indian",             "Madeira",
      "Mariemont FD",               "Mariemont",
      "Miami Twp FD",               "North Bend",
      "Milford FD",                 "Milford",
      "Montgomery FD",              "Montgomery",
      "Mt Healthy FD",              "Mt Healthy",
      "N College Hill",             "N College Hill",
      "N College Hill FD",          "N College Hill",
      "Norwood FD",                 "Norwood",
      "Reading FD",                 "Reading",
      "Sharonville FD",             "Sharonville",
      "Springdale FD",              "Springdale",
      "Springfield Twp FD",         "Springfield Twp",
      "Springfield Twp",            "Springfield Twp",
      "St Bernard FD",              "St Bernard",
      "Sycamore Twp FD",            "Sycamore Twp",
      "Sycamore Twp",               "Sycamore Twp",
      "Terrace Park FD",            "Terrace Park",
      "Union Twp FD",               "Union Twp",
      "Warren CO FD",               "Warren County",
      "West Chester FD",            "West Chester",
      "Whitewater Twp FD",          "Whitewater Twp",
      "Whitewater Twp",             "Whitewater Twp",
      "Woodlawn FD",                "Woodlawn",
      "Wyoming FD",                 "Wyoming"
  );
}
