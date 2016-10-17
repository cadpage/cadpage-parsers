package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCJacksonCountyParser extends DispatchSouthernParser {
  
  private static final Pattern GEN_ALERT_ADDRESS_PTN = Pattern.compile("(?:(?:NORTHERN|SOUTHERN|EASTERN|WESTERN) )?JACKSON CO(?:UNTY)?(?: WEATHER)?|UNIT +\\d+|\\d+ +BASE|.* FIRE DEPT|.* FD|1");
  
  
  public NCJacksonCountyParser() {
    super(CITY_LIST, "JACKSON COUNTY", "NC", DSFLAG_OPT_DISPATCH_ID | DSFLAG_LEAD_PLACE | DSFLAG_TRAIL_PLACE | DSFLAG_FOLLOW_CROSS);
    setupMultiWordStreets(MWORD_STREET_LIST);
    addRoadSuffixTerms("PLZ");
  }
  
  @Override
  public String getFilter() {
    return "Bill@mydomain.com,232@jacksonrescue.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_CR_CREEK;
  }
  

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (! super.parseMsg(body, data)) return false;
    
    if (data.strPlace.length() > 0 && isValidAddress(data.strPlace)) {
      data.strAddress = append(data.strAddress, " & ", data.strPlace);
      data.strPlace = "";
    }
    
    if (data.strApt.equals("0")) data.strApt = "";
    
    // Weather alerts show up as dispatched calls rather than general alerts
    if (data.strSupp.startsWith("WEATHER ") || GEN_ALERT_ADDRESS_PTN.matcher(data.strAddress).matches()) {
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = append(data.strCall, " ", data.strSupp);
      data.strCall = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" AT ", " & ");
      super.parse(field, data);
    }
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_CROSS_FOLLOWS;
  }

  private static final Pattern PINE_CONE_PTN = Pattern.compile("\\bPINE +CONE\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PINE_CONE_PTN.matcher(addr).replaceAll("PINECONE");
    return super.adjustMapAddress(addr);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ADDIE CEMETERY",
    "ALBURY POINT",
    "ALLEN HENSON",
    "ALLENS BRANCH",
    "ANDREWS PARK",
    "ASHLEY CAGLE",
    "BACK NINE",
    "BALD ROCK",
    "BALSAM DEPOT",
    "BALSAM LOOP",
    "BARKERS CREEK",
    "BEE KNOB CEMETERY",
    "BENDED KNEE",
    "BERRY HILL",
    "BIG MOUNTAIN",
    "BIG SHEEPCLIFF",
    "BIG WOODS",
    "BILLY COVE",
    "BLACK OAK",
    "BLACK ROCK RANCH",
    "BLANTON BRANCH",
    "BLUE OVAL",
    "BLUE RIDGE",
    "BRADLEY BRANCH",
    "BREEZY MOUNTAIN",
    "BRIGHT MOUNTAIN",
    "BROOKS BRANCH",
    "BRUSHY FORK",
    "BUFF CREEK",
    "BULL PEN",
    "BULLS GAP",
    "BUZZARDS ROOST",
    "CABIN FLATS",
    "CAMP CR",
    "CAMP CREEK",
    "CANARY SPRINGS",
    "CANE CREEK",
    "CEDAR CREEK",
    "CHAD CRAWFORD",
    "CHAPEL VIEW",
    "CHARLEYS CREEK",
    "CHATTOOGA RIDGE",
    "CHATTOOGA WOOD",
    "CHIMNEY TOP",
    "CHIPPER CURVE",
    "COPE CREEK",
    "COUNTRY CLUB",
    "COURTHOUSE TERRACE",
    "COWAN VALLEY",
    "CRAWFORD CEMETERY",
    "CROSS CREEK",
    "CROWN POINT",
    "CRYSTAL SPRING",
    "CULLOWHEE FOREST",
    "CULLOWHEE MOUNTAIN",
    "CURLY WILLOW",
    "CUTTING EDGE",
    "DARK RIDGE",
    "DEER RUN",
    "DICKS CREEK",
    "DOUBLE H",
    "DREAM VALLEY",
    "DUMMY LINE",
    "EAGLE RIDGE",
    "EAST SYLVA",
    "EAST VALLEY",
    "EAST VIEW",
    "FAIRWAY FOREST",
    "FISHER CREEK",
    "FLAT BOTTOM",
    "FLAT ROCK",
    "FOREST ROSE",
    "FOUND FOREST",
    "FRANK ALLEN",
    "FRASER FIR",
    "GARDEN VIEW",
    "GLAD TIDINGS",
    "GRAND OAKS",
    "GREENS CREEK",
    "GRINDSTAFF COVE",
    "GRINDSTONE KNOB",
    "HANGING ROCK",
    "HAPPY RIDGE",
    "HAZEL HOLLOW",
    "HEADY MOUNTAIN",
    "HIDDEN VALLEY",
    "HIGH CLIMBER",
    "HIGH MOUNTAIN",
    "HIGH RIDGE",
    "HIGHLANDS COVE",
    "HIGHLANDS WILSON",
    "HOLLY FOREST",
    "HOLLY HILLS",
    "HONEY BEE",
    "HOUND DOG",
    "HURRICANE CREEK",
    "INT KINGS CREEK",
    "INT STORYBOOK",
    "IRON HORSE",
    "JIM SELLERS",
    "JINGLE BELL",
    "JOE PATCH",
    "JOE PYE",
    "JOHNS CREEK",
    "JOLLY HILLS",
    "KETTLE CREEK",
    "LAKE RIDGE",
    "LAKE SIDE",
    "LAUREL KNOB",
    "LEAF GAZER",
    "LEVI MATHIS",
    "LICK LOG",
    "LITTLE SAVANNAH",
    "LLOYD HOOPER",
    "LOFTY MOUNTAIN",
    "LOGAN CREEK",
    "LONE CHIMNEY",
    "LONE STAR",
    "LONELY VIOLET",
    "LONESOME DOVE",
    "LONESOME VALLEY",
    "LONG POND",
    "LONG SPLICE",
    "LYLE WILSON",
    "MACS VIEW",
    "MILL CREEK",
    "MILLS BRANCH",
    "MIRROR LAKE",
    "MONTE VISTA",
    "MONTEITH GAP",
    "MOODY BRIDGE",
    "MOUNT EIRE",
    "MOUNT HARBOR CLUB",
    "MOUNT MITCHELL",
    "MOUNT PLEASANT CHURCH",
    "MOUNTAIN HERITAGE",
    "MOUNTAIN TRACE",
    "NAPA RIDGE",
    "NATIONS CREEK",
    "NEDDIE MOUNTAIN",
    "NICHOLSON COVE",
    "NICOL ARMS",
    "NODDING WREN",
    "NORTH FORK CREEK",
    "NORTH FORK",
    "NORTH RIVER",
    "NORTH STAR",
    "OLIVET CHURCH",
    "OWEN MTN",
    "PARRIS BRANCH",
    "PAW PAW",
    "PEBBLE CREEK",
    "PEE WEE BRANCH",
    "PINE CONE",
    "PINE CREEK",
    "PINEY MOUNTAIN",
    "PINEY MTN",
    "PLEASANT GROVE CHURCH",
    "POSEY BLANTON",
    "POTTS COMMUNITY",
    "PRESSLEY CREEK",
    "RED BRIDGE",
    "RED RED",
    "RIO BRAVO",
    "ROCK QUARRY",
    "ROUGH BARK",
    "RUFUS ROBINSON",
    "RUSHING BROOK",
    "SAPPHIRE VALLEY",
    "SCOTTS CREEK",
    "SHERWOOD FOREST",
    "SHOOK COVE",
    "SHOOTING STAR",
    "SHORTLEAF PINE",
    "SKY MOUNTAIN",
    "SLO GAIT",
    "SMOKY MOUNTAIN",
    "SNUG HAVEN",
    "SOUTH RIVER",
    "SPARK CHASER",
    "SPEEDWELL BAPTIST CHURCH",
    "SPICEWOOD VALLEY",
    "SPLIT RAIL",
    "SPRING VALLEY",
    "SQUIRREL HUNTIN",
    "STONE CLIFFS",
    "STOVE PIPE",
    "SUGAR LOAF",
    "SUMMER HILL",
    "SUNSET FARM",
    "TALL OAKS",
    "TANNASSEE CREEK",
    "TATHAMS CREEK",
    "TEIGUE COVE",
    "THREE PONDS",
    "TIGER PAW",
    "TILLEY CREEK",
    "TIMBER RIDGE",
    "TOLL HOUSE",
    "TOY COVE",
    "TRAYS ISLAND",
    "TRILLIUM RIDGE",
    "TROUT CREEK",
    "TUNNEL MOUNTAIN",
    "TURTLE CREEK",
    "TUTTLE MINK",
    "VALLEY MIST",
    "VISTA FALLS",
    "WAKE ROBIN",
    "WALTER ASHE",
    "WEST GATE",
    "WHISPER RIVER",
    "WHISTLE STOP",
    "WHITESIDE COVE",
    "WHITEWATER FALLS",
    "WILD HORSE",
    "WINDY GAP",
    "WOLF LAKE",
    "WOLF MOUNTAIN",
    "WOODY HAMPTON",
    "YELLOW BIRD BRANCH",
    "YELLOW MOUNTAIN",
    "YELLOW MTN"
  };


  private static final String[] CITY_LIST = new String[]{

    // Cities
    "DILLSBORO",
    "HIGHLANDS",
    "SYLVA",
    "WEBSTER",
    "CULLOWHEE",

    // Village
    "FOREST HILLS",

    // Census-designated places
    "CASHIERS",
    "CHEROKEE",
    "GLENVILLE",

    // Unincorporated communities
    "ADDIE",
    "BALSAM",
    "BETA",
    "GAY",
    "SAVANNAH",
    "TUCKASEGEE",
    "WHITTIER",
    "WILLETS",
    "WILMOT",
    
    // Haywood County
    "MAGGIE VALLEY",
    
    // Transylvania County
    "SAPPHIRE"
    
  };

}
