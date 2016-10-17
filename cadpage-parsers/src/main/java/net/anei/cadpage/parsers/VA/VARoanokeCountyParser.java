package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class VARoanokeCountyParser extends SmartAddressParser {
  
  private static final Pattern MSG_HEADER_PTN = Pattern.compile(">>> <dispatch@roanokecountyva.gov> (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) >>>\n\n");
  private static final Pattern MASTER_PTN1 = Pattern.compile("(.*?)  (\\d{4}) (.*)(Roanoke County|Floyd County|Town of Vinton) ([ A-Z]+) (\\d{4} \\d{8})");
  private static final Pattern NOT_DISPATCH_PTN = Pattern.compile("\\b(?:ADV|TRAINING)\\b");
  private static final Pattern DATE_TIME_PTN1 = Pattern.compile("[ \n]*([12]?\\d/\\d{1,2}/\\d{4}) +(\\d{1,2}:\\d{1,2}:\\d{1,2} [AP]M)$");
  private static final Pattern DATE_TIME_PTN2 = Pattern.compile("[ \n]*([12]?\\d-[A-Z]{3}-\\d{4}) +(\\d{1,2}:\\d{1,2}:\\d{1,2})$", Pattern.CASE_INSENSITIVE);
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat DATE_FMT2 = new SimpleDateFormat("dd-MMM-yyyy");
  private static final Pattern XST_PTN = Pattern.compile("[- ]+X ?ST(?:REETS?)?\\b:? *");
  private static final Pattern X_APT_PTN1 = Pattern.compile("(?:APT|RM|(FL|LOT))(?:\\b|(?=\\d)) *([^ ]+)\\b *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern X_APT_PTN2 = Pattern.compile("([A-Z]?\\d+(?: [A-DF-H])?|[A-DF-H])\\b(?! +(?:AVE?|ST)\\b) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern X_NO_CROSS_PTN = Pattern.compile("(.*?) *\\bNo Cross Streets Found\\b *(.*)");
  private static final Pattern UNIT_PTN = Pattern.compile("^((?:[A-Z]+\\d+|SALEMEMS|SALEMF|RES(?:CUE)?[- ]\\d+)(?:\\$[A-Z]+\\d+)? +)+");
  private static final Pattern X_PHONE_PTN = Pattern.compile("((?:\\(?\\d{3}\\)? ?)?\\d{3}[- ]\\d{4})\\b *(.*)");
  private static final Pattern APT_PHONE_PTN = Pattern.compile("\\b(?:\\(?\\d{3}\\)? ?)?\\d{3}[- ]\\d{4}\\b");
  private static final Pattern APT_PTN = Pattern.compile("([A-Z]{2}|[A-Z]?\\d+[A-Z]?)\\b *(.*)");
    
  
  public VARoanokeCountyParser() {
    super("ROANOKE COUNTY", "VA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = GRANDIN_ROAD_EXT.matcher(address).replaceAll("$1 EXD");
    return super.adjustMapAddress(address);
  }
  private static final Pattern GRANDIN_ROAD_EXT = Pattern.compile("\\b(GRANDIN ROAD) EXT\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Convert some misplaced control characters
    body = body.replace("\u0001", "").replace('\u0004', '\n').trim();
    
    // And any email headers that get through :(
    if (body.startsWith("Received:")) {
      int pt = body.indexOf("\n\n");
      if (pt < 0) return false;
      body = body.substring(pt+2).trim();
    }
    
    // And non-standard message headings
    Matcher match = MSG_HEADER_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      body = body.substring(match.end()).trim();
    }
    
    // There seem to be two different formats, possibly separated chronologically
    // This one can be identified by a pattern match
    match = MASTER_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("DATE TIME UNIT BOX ADDR APT CITY CALL ID");
      data.strUnit = match.group(1).trim();
      data.strBox = match.group(2);
      parseAddress(match.group(3).trim(), data);
      String city = match.group(4);
      if (city.startsWith("Town of ")) city = city.substring(8).trim();
      data.strCity = city;
      data.strCall = match.group(5).trim();
      data.strCallId = match.group(6);
      return true;
    }
    
    // Second format tends to catch a lot of things that really are not
    // dispatch pages.  We will look for some keywords that indicate this
    // is not a dispatch alert
    if (NOT_DISPATCH_PTN.matcher(body).find()) return false;
    
    setFieldList("UNIT CALL PLACE ADDR APT PHONE INFO X DATE TIME");
    boolean good = false;
    match = DATE_TIME_PTN1.matcher(body);
    if (match.find()) {
      data.strDate = match.group(1);
      setTime(TIME_FMT1, match.group(2), data);
      body = body.substring(0,match.start()).trim();
      good = true;
    }
    else if ((match = DATE_TIME_PTN2.matcher(body)).find()) {
      setDate(DATE_FMT2, match.group(1), data);
      data.strTime = match.group(2);
      body = body.substring(0,match.start()).trim();
      good = true;
    }
    
    String bodyUpsh = body.toUpperCase();
    match = XST_PTN.matcher(bodyUpsh);
    if (match.find()) {
      String cross  = body.substring(match.end());
      body = body.substring(0,match.start());
      good = true;
      
      match = X_APT_PTN1.matcher(cross);
      if (match.matches()) {
        data.strApt = append(getOptGroup(match.group(1)), " ", match.group(2));
        cross = match.group(3);
      }
      else if ((match = X_APT_PTN2.matcher(cross)).matches()) {
        data.strApt = match.group(1).replace(' ', '-').trim();
        cross = match.group(2).trim();
      }
      match = X_PHONE_PTN.matcher(cross);
      if (match.matches()) {
        data.strPhone = match.group(1);
        cross = match.group(2);
      }
      match = X_NO_CROSS_PTN.matcher(cross);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        data.strPlace = append(data.strPlace, " - ", match.group(2));
      } else { 
        cross = cross.replaceAll("  +", " / ");
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT, cross, data);
        String left = getLeft();
        left = stripFieldStart(left, "-");
        if (left.startsWith("/")) {
          left = left.substring(1).trim();
          String saveCross = data.strCross;
          data.strCross = "";
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, left, data);
          data.strCross = append(saveCross, " / ", data.strCross);
          data.strPlace = append(data.strPlace, " - ", getLeft());
        } else if (isValidAddress(left)) {
            data.strCross = append(data.strCross, " / ", left);
        } else  { 
          data.strPlace = append(data.strPlace, " - ", left);
        }
      }
    }
    
    match = UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strUnit = match.group().trim();
      body = body.substring(match.end()).trim();
      good = true;
    }
    
    parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ | FLAG_IGNORE_AT | FLAG_START_FLD_NO_DELIM, body.toUpperCase(), data);
    if (data.strAddress.length() == 0) {
      if (data.strPlace.length() == 0) return false;
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    if (!good && CALL_LIST.getCode(data.strCall, true) == null) return false;
    data.strPlace = stripFieldStart(data.strPlace, "(");
    data.strPlace = stripFieldStart(data.strPlace, ",");
    data.strPlace = stripFieldEnd(data.strPlace, "-");
    String left = getLeft();
    left = stripFieldStart(left, "-");
    if ((match = APT_PHONE_PTN.matcher(left)).find()) {
      data.strApt = append(data.strApt, "-", left.substring(0,match.start()).trim());
      data.strPhone = match.group(0);
      data.strSupp = left.substring(match.end()).trim();
    }
    else if ((match = APT_PTN.matcher(left)).matches()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      data.strSupp = match.group(2);
    } else {
      data.strSupp = left;
    }
    
    // See if we should split a place name from the call description
    if (data.strPlace.length() == 0) {
      int pt = data.strCall.indexOf('(');
      if (pt >= 0) {
        data.strPlace = data.strCall.substring(pt+1).trim();
        if (data.strPlace.endsWith(")")) data.strPlace = data.strPlace.substring(0,data.strPlace.length()-1).trim(); 
        data.strCall = data.strCall.substring(0,pt).trim();
      }
    }
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "AMERICAN TIRE",
    "APPLE GROVE",
    "APPLE HARVEST",
    "AUTUMN PARK",
    "BACK CREEK ORCHARD",
    "BEAR RIDG",
    "BEAR RIDGE",
    "BELLE MEADE",
    "BENDING OAK",
    "BENT MOUNTAIN",
    "BLUE VIEW",
    "BOTTOM CREEK",
    "BRIAR HILL",
    "BROAD HILL",
    "BUCK MOUNTAIN",
    "BUCK RUN",
    "BUSH FARM",
    "CARDINAL PARK",
    "CARRIAGE HILLS",
    "CARVINS COVE",
    "CASTLE HILL",
    "CASTLE ROCK",
    "CATAWBA FOREST",
    "CATAWBA HOSPITAL",
    "CAVE SPRING",
    "CHARING CROSS",
    "CIRCLE BROOK",
    "CLEARBROOK PARK",
    "CLEARBROOK VILLAGE",
    "CLOVER HILL",
    "COOK CREEK",
    "COTTAGE ROSE",
    "COTTON HILL",
    "COUNTRY HOMES",
    "CRYSTAL CREEK",
    "CYPRESS PARK",
    "DEER BRANCH",
    "DEER PATH",
    "DEER RIDGE",
    "DREWRYS HILL",
    "EAGLE CREST",
    "EAST VIRGINIA",
    "ELM VIEW",
    "FAIRWAY FOREST",
    "FAIRWAY RIDGE",
    "FAIRWAY WOODS",
    "FEATHER GARDEN",
    "FOREST EDGE",
    "FOREST VIEW",
    "FORTUNE RIDGE",
    "GARST MILL PARK",
    "GARST MILL",
    "GLADE CREEK",
    "GLEN HAVEN",
    "GLEN HEATHER",
    "GOLDEN IVY",
    "GREEN HOLLOW",
    "GREEN MEADOW",
    "GREEN RIDGE",
    "GREEN VALLEY",
    "GREY FOX",
    "HARVEST HILL",
    "HICKORY HILL",
    "HICKORY RIDGE",
    "HIDDEN HILL",
    "HIDDEN VALLEY SCHOOL",
    "HIDDEN VALLEY",
    "HIDDEN WOODS",
    "HORSEPEN MOUNTAIN",
    "HUFF MILL",
    "HUNTING HILLS",
    "HYDE PARK",
    "INDIAN GRAVE",
    "IVY RIDGE",
    "JAE VALLEY",
    "LAUREL CREEK",
    "LAUREL GLEN",
    "LOCH HAVEN",
    "LOST MOUNTAIN",
    "LOST VIEW",
    "LOUISE WELLS",
    "LYNN HAVEN",
    "LYNNN HAVEN",
    "MALLARD LAKE",
    "MANSARD SQUARE",
    "MARTINS CREEK",
    "MCVITTY FOREST",
    "MEWS HILL",
    "MILL FOREST",
    "MILL PLANTATION",
    "MILL RUN",
    "MOCKINGBIRD HILL",
    "MOUNT PLEASANT",
    "MOUNTAIN HEIGHTS",
    "MOUNTAIN VIEW",
    "MT CHESTNUT",
    "NORTH ELECTRIC",
    "NORTH GARDEN",
    "NORTHSIDE HIGH SCHOOL",
    "OLD CAVE SPRING",
    "OLD MILL PLANTATION",
    "ORCHARD PARK",
    "ORCHARD VIEW",
    "ORCHARD VILLAS",
    "PAST TIMES",
    "PENN FOREST",
    "PETERS CREEK",
    "PINE ACRES",
    "PINEV D",
    "PLANTATION GROVE",
    "PLEASANT HILL",
    "POAGE VALLEY RD",
    "POAGE VALLEY",
    "POAGES MILL",
    "POOR MOUNTAIN",
    "POPLAR SPRINGS",
    "PURPLE FINCH",
    "RAMSEY BLVD",
    "RAN LYNN",
    "RED CEDAR",
    "RED LANE",
    "RIDGELEA ESTATES",
    "SCARLET OAK",
    "SCENIC HILLS",
    "SHINGLE RIDGE",
    "SILVER LEAF",
    "SLINGS GAP",
    "SOUTH BARRENS",
    "SOUTH MOUNTAIN",
    "SOUTH PACIFIC",
    "SOUTH PEAK",
    "SOUTH ROSELAWN",
    "SPRADLIN LOOP",
    "SPRING GROVE",
    "SPRING HOLLOW ACCESS",
    "SPRING MEADOW",
    "SPRING RUN",
    "SPRINGWOOD PARK",
    "STERLING PLACE",
    "STILL BRANCH",
    "STONE MOUNTAIN",
    "STONELYN COTTAGE",
    "STONEY RIDGE",
    "SUGAR LOAF MOUNTAIN",
    "SUGAR LOAF MOUNTEI^",
    "SUGAR RIDGE",
    "SUNNY SIDE",
    "TANGLEWOOD EAST ENTRANCE",
    "TANGLEWOOD WEST ENTRANCE",
    "TIMBER BRIDGE",
    "TREE TOP CAMP",
    "TREE TOP",
    "TWELVE OCLOCK KNOB",
    "TWIN VIEWS",
    "TWINE HOLLOW",
    "VA SPRUCE",
    "VALE HILL",
    "VALLEY FORGE",
    "VALLEY GATEWAY",
    "VALLEY VIEW",
    "WALMART CHALLENGER FRONT ENTRANCE",
    "WEST MAIN",
    "WEST RIVER",
    "WEST RURITAN",
    "WEST VIRGINIA",
    "WESTWARD LAKE",
    "WHITE PELICAN",
    "WILLOW LEAF",
    "WILLOW SPRING",
    "WIND SONG",
    "WOOD HAVEN",
    "YELLOW MOUNTAIN"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACC  INJ",
      "ACC INJ",
      "ACCIDENT",
      "ACCIDENT MINOR INJURY",
      "ACCIDENT MINOR INJURY PD ONSCENE",
      "ACCIDENT PERSONAL INJURY",
      "ALARMC",
      "ALARMR",
      "ALS",
      "ALS UPGRADED ALSC",
      "ALS-CHEST PAINS",
      "ALSC",
      "ALS CRITICAL",
      "ASSISTF",
      "ASSISTR",
      "BLS",
      "BRUSH",
      "BRUSH FIRE",
      "CAN DISREGARD",
      "CARBON",
      "CARBON MONOXIDE LEAK",
      "CHILD LOCKED IN VEHICLE",
      "CHIMNEY",
      "CHIMNEY FIRE",
      "CLSC",
      "CODE BLUE",
      "COMMERCIAL FIRE ALARM",
      "COMMERCIAL GAS LEAK",
      "COMMERCIAL STRUCTURE FIRE",
      "DISREGARD",
      "DUMPSTER",
      "ELEVATOR",
      "EMS SERVICE CALL",
      "ESERVICE",
      "FIRE ALARM",
      "FIRE SERVICE CALL",
      "FSERVICE",
      "GASC",
      "GASR",
      "HAZMAT",
      "HAZMAT RESPONSE",
      "HIT & RUN PERSONAL INJURY",
      "ILLEGAL BURN",
      "ILLEGALBURN",
      "RESIDENTIAL FIRE ALARM",
      "RESIDENTIAL GAS LEAK",
      "RESIDENTIAL STRUCTURE FIRE",
      "SECOND EMERGENCY CALL ALS",
      "SERVICE CALL",
      "SMOKE",
      "SMOKE REPORT",
      "STANDBY",
      "STRUCTC",
      "STRUCTR",
      "TECHRES",
      "TEST CALL",
      "TEST PUBLIC SAFETY CENTER",
      "TEST STATION 2",
      "TEST STATION 3",
      "TEST STATION 5",
      "TEST STATION 11",
      "THEFT",
      "TREE ON A LINE",
      "UNITS ON SCENE",
      "UPGRADED ALSC",
      "VEHICLE",
      "VEHICLE FIRE",
      "WIRE DOWN",
      "WIREDOWN",
      
      // One time call descriptions
      "HIKER DRAGONS TOOTH"
  );
}
