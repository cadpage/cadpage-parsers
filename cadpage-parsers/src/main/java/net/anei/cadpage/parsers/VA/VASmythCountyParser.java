package net.anei.cadpage.parsers.VA;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class VASmythCountyParser extends DispatchSouthernParser {
  
  public VASmythCountyParser() {
    super(CALL_LIST, CITY_LIST, "SMYTH COUNTY", "VA", 
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_NAME | DSFLG_OPT_PHONE | DSFLG_ID | DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("INTER HWY 11");
  }

  @Override
  public String getFilter() {
    return "@smythcounty.org";
  }
  
  private static final Pattern APT_SECTOR_PTN = Pattern.compile("(.*?) *\\b((?:[NSEW] |[NS][EW] )?SECT(?:OR)?)");
  private static final Pattern TRAIL_DIR_PTN = Pattern.compile("(.*?) +([NSEW]|[NS][EW])");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = APT_SECTOR_PTN.matcher(data.strApt);
    if (match.matches()) {
      data.strApt = match.group(1).trim();
      String sector = match.group(2);
      if (!sector.contains(" ")) {
        match = TRAIL_DIR_PTN.matcher(data.strAddress);
        if (match.matches()) {
          data.strAddress = match.group(1);
          sector = match.group(2) + ' ' + sector;
        }
      }
      data.strMap = sector;
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("APT", "APT MAP");
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ALLISON GAP",
    "APPLE VALLEY",
    "B F BUCHANAN",
    "BEAVER CREEK",
    "BETTER HOMES",
    "BLUFF HOLLOW",
    "BONE HOLLOW",
    "BRUSHY MOUNTAIN",
    "BUCKEYE HOLLOW",
    "BURGESS HOLLOW",
    "CARLOCK CREEK",
    "CAVE RIDGE",
    "CEDAR BRANCH",
    "CEDAR CREEK",
    "CEDAR SPRINGS",
    "CHATHAM HILL",
    "CHESTNUT RIDGE",
    "CLEGHORN VALLEY",
    "COMERS CREEK",
    "COUNTRY LIVING",
    "CROSS CREEK",
    "CURRIN VALLEY",
    "DEER HOLLOW",
    "DICKEYS CREEK",
    "DOANE HOLLOW",
    "DRY COVE",
    "EAST LEE",
    "EAST MAIN",
    "ENDLESS VIEW",
    "FALCON PLACE",
    "FINLEY GAYLE",
    "FLAT RIDGE",
    "FOREST HILL",
    "FRANCIS MARION",
    "FREEDOM TABERNACLE",
    "FREESTONE VALLEY",
    "GAILLIOT VISTA",
    "HARMONY HILLS",
    "HAYTERS GAP",
    "HORNE HOLLOW",
    "HORSESHOE BEND",
    "HURRICANE CAMPGROUND",
    "INDIAN RUN",
    "JOHNSTON MEMORIAL",
    "KELLY CHAPEL",
    "KELLY HILL",
    "LAUREL VALLEY",
    "LEAD MINE",
    "LICK SKILLET",
    "LIONS CLUB",
    "LITTLE VALKER",
    "LOGAN CREEK",
    "LOVES MILL",
    "LYONS GAP",
    "MAPLE LEAF",
    "MARION MANOR",
    "MEDICAL PARK",
    "MIDDLE FORK",
    "MONTE VISTA",
    "MOORE CREEK",
    "MOUNT CALM",
    "MOUNTAIN EMPIRE",
    "NEBO MOUNTAIN",
    "NORTH FORK RIVER",
    "NORTH OVERLOOK",
    "OAK POINT",
    "PICKLE HOLLOW",
    "PLUM CREEK",
    "POORE VALLEY",
    "POSSUM HOLLOW",
    "PUGH MOUNTAIN",
    "QUAIL RUN",
    "QUARTER BRANCH",
    "REED HOLLOW",
    "RIVER BOTTOM",
    "ROBERTS CHAPEL",
    "ROCKY HOLLOW",
    "SCRATCH GRAVEL",
    "SEVEN SPRINGS",
    "SHALE BANK",
    "SHANNON GAP",
    "SHEEP HILL",
    "SHULER HOLLOW",
    "SLAB TOWN",
    "SLEMP CREEK",
    "SMYTH CHAPEL",
    "SOUTH MAIN",
    "SPRING HOLLOW",
    "ST CLAIRS CREEK",
    "STAR MOUNTAIN",
    "STILL HOUSE HOLLOW",
    "SUGAR GROVE",
    "SUGAR SHACK",
    "SULPHUR SPRINGS",
    "TATTLE BRANCH",
    "THOMAS BRIDGE",
    "TOWN SPRINGS",
    "TREE TOP",
    "TRIPPLE TEAL",
    "WALKERS CREEK",
    "WEST LEE",
    "WEST MAIN",
    "WIDENER VALLEY",
    "WORLEY RICH"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP",
      "ABDOMINAL PAIN-INJURY",
      "ABDOMINAL-PAIN",
      "AGENCY-ASSISTANCE",
      "ALARM",
      "ALARM-FIRE",
      "ALARM-MEDICAL",
      "ALLERGIC",
      "ALERGIC REACTION",
      "ARGUMENT",
      "ASSAULT",
      "BACK PAIN",
      "BACK-PAIN-INJURY",
      "BLEEDING",
      "BOMB THREAT",
      "BREATHING ABNORMALLY",
      "BREATHING-DIFFICULTY",
      "BRUSH-FIRE",
      "CARDIAC CALL",
      "CHEST PAINS",
      "CHEST-PAIN",
      "CHOKING",
      "CHOKING CHILD",
      "CPR-ADULT->8-YRS",
      "CPR-INFANT-<1-YR BABY",
      "DEAD ON ARRIVAL",
      "DEATH",
      "DEBRIS",
      "DEHYDRATED",
      "DIABETIC",
      "DIFFICULTY IN BREATHING",
      "DISABLED-VEHICLE",
      "DISORIENTED",
      "DISTURBANCE",
      "DIZZY",
      "DOA",
      "DOMESTIC",
      "DOWN POWER LINE",
      "DUPLICATE-CALL",
      "ELEVATOR EMERGENCY",
      "EXPLOSION",
      "FALL",
      "FALL VICTIM",
      "FEVER",
      "FIGHT",
      "FIRE ALARM",
      "FIRE ALL TYPES",
      "FIRE-GENERAL",
      "FIRE-REKINDLE",
      "FIRE/OTHER",
      "GAS-LEAK",
      "GUN-FIRE",
      "HEAD-INJURY",
      "HEADACHE",
      "HEAT-ENVIRONMENTAL-EMERG",
      "INFORMATION ONLY",
      "INFORMATION-GENERAL",
      "INOPERATIVE VEHICLE",
      "INVESTIGATION",
      "JUVENILE",
      "LIFT-ASSIST",
      "MARCUS Level 3",
      "MEDICAL ALARM",
      "MENTAL-HEALTH-COMPLAINT",
      "MESSAGE",
      "MISCELLANEOUS-CALL",
      "MVC",
      "MVC UNK INJ",
      "NEEDS RESCUE SQUAD",
      "OBSTETRICAL-EMERGENCY",
      "OVERDOSE",
      "PAIN",
      "PAIN-GENERAL",
      "PUBLIC SERVICE",
      "PURSUIT-OF-MOTOR-VEHICLE",
      "RAPE",
      "RECKLESS-DRIVING",
      "RESCUE",
      "Road Closure",
      "SEIZURE",
      "SEIZURES",
      "SICK",
      "SICK CALL/AMB NEEDED",
      "SMOKE-REPORT",
      "STROKE",
      "STROKE-POSSIBLE",
      "STRUCTURE FIRE",
      "SUICIDE",
      "SUSPICIOUS",
      "SYNCOPE",
      "THREATS",
      "TRAFFIC ACCIDENT",
      "TRAFFIC-HAZARD",
      "TRANSPORT",
      "TRAUMA",
      "UNCONSCIOUS-PERSON",
      "UNKNOWN-MEDICAL-CALL",
      "UNKNOWN-NATURE-OF-CALL",
      "UNKNOWN PROBLEM",
      "UNRESPONSIVE",
      "UTILITIES",
      "VANDALISM",
      "VEHICLE-FIRE",
      "VOMITTING",
      "YOM LOW SODIUM",
      "WEATHER-RELATED",
      "WELFARE-CHECK",
      "WORKING-STRUCTURE-FIRE",
      "WRONG WAY DRIVER",
      
      // One time free form call descriptions
      "MALE SUBJ HAS COLLAPSED"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "CHILHOWIE",
    "MARION",
    "SALTVILLE",
    
    "ADWOLF",
    "ATKINS",
    "SEVEN MILE FORD",
    "SUGAR GROVE",
    
    "BROADFORD",
    "GROSECLOSE",
    "RICH VALLEY",
    
    // Bland County
    "CERES",
    
    // Tazewell County
    "TANNERSVILLE",
    
    // Washington County
    "ABINGDON",
    "BRISTOL",
    "DAMASCUS",
    "EMORY",
    "GLADE SPRING",
    "MEADOWVIEW",
    
    // Wythe County
    "RURAL RETREAT"
  }; 
}
