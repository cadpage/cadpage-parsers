package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;



public class NCMaconCountyParser extends DispatchB3Parser {
  
  private static final Pattern PRIMARY_PREFIX_PTN = Pattern.compile("MACON 911 MACON911:|MACON 911: MACON911:|911 CENTER:|MACON911:|MACON911 ");
 
  public NCMaconCountyParser() {
    super(PRIMARY_PREFIX_PTN, CITY_LIST, "MACON COUNTY", "NC");
    removeWords("DRIVE");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "MACON911@maconnc.org,4702193684,8283420118,8283711473,4702193684";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace('~', ' ').trim();
    body = stripFieldEnd(body, " Stop");
    if (! super.parseMsg(subject, body, data)) return false;
    data.strCross = data.strCross.replace('@', '/');
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ADDINGTON BRIDGE",
    "ANGEL COVE",
    "BAIRD COVE",
    "BAIRD CV",
    "BELLE DOWDLE",
    "BETHEL CHURCH",
    "BREEZY KNOB",
    "BROOKSTONE FOREST",
    "BROOKSTONE VISTA",
    "BRYSON CITY",
    "BRYSON CTY",
    "BUSTER CABE",
    "CARL AMMONS",
    "CAT CREEK",
    "CHURCH HILL",
    "CLARKS CHAPEL",
    "CLEAR STREAM",
    "COUNTRY CLUB",
    "COUNTRY WOODS",
    "COWEE CREEK",
    "COWEETA CHURCH",
    "CRISP COUNTRY",
    "DAY CARE",
    "DOWDLE MOUNTAIN",
    "FISH HAWK",
    "FOX LAYRE",
    "FOX RIDGE",
    "FOX RUN",
    "GINGER TREE",
    "GOLD CITY",
    "GOLF VIEW",
    "GRANDVIEW ACRES",
    "GREEN KNOB",
    "HAYES MILL",
    "HEATHERS COVE",
    "HERITAGE HOLLOW",
    "HIDDEN CREEK",
    "HIGHLAND WOODS",
    "HILAND PARK",
    "HOLLY HILLS VISTA",
    "HOLLY SPRINGS CHURCH",
    "HOLLY SPRINGS EST",
    "INDUSTRIAL PARK",
    "JACK CABE",
    "JIM BERRY",
    "JOE BRADLEY",
    "JOHN JUSTICE",
    "KINSLAND PARK",
    "LAKE EMORY",
    "LAUREL LAKE",
    "LAVONA JOY",
    "LEATHERMAN GAP",
    "LEE TALLENT",
    "LLOYD TALLENT",
    "LOG CABIN",
    "LOUISA CHAPEL",
    "LYLE DOWNS",
    "MAPLES PARK",
    "MASHBURN BR",
    "MASHBURN BRANCH",
    "MASHBURN WHITE",
    "MIDDLE SKEENAH",
    "MISTY MEADOW",
    "MISTY RIDGE",
    "MOUNTAIN SPRINGS",
    "NOAH GIBSON",
    "NORTH BLAINE BRANCH",
    "NORTH SKEENAH",
    "OAK HILL",
    "ONE CENTER",
    "ORCHARD VIEW",
    "PEEKS CREEK",
    "PETE MCCOY",
    "PHILLIPS ST TO",
    "POINT PLEASANT",
    "PRENTISS BRIDGE",
    "PRESSLEY CIRCLE",
    "RABBIT CREEK",
    "RAY DOWNS",
    "RIVER VALLEY",
    "ROLLER MILL",
    "ROSE CREEK",
    "SAM STOCKTON",
    "SANDERSTOWN RIDGE",
    "SHADY BROOK",
    "SHORT RIDGE",
    "SILVER SPRUCE",
    "SNOW HILL",
    "SOUTH FRONT",
    "SUGARFORK CHURCH",
    "T BIRD",
    "TERRACE RIDGE",
    "TOWN MOUNTAIN",
    "TRIMONT MOUNTAIN",
    "TURTLE POND",
    "ULCO BLUFFS",
    "UP THE CREEK",
    "VALLEY VIEW",
    "VAN RAALTE",
    "WALLACE BRANCH",
    "WALNUT COVE",
    "WELLS GROVE",
    "WEST DILLS CREEK",
    "WEST OLD MURPHY",
    "WHITE PINE",
    "WIDE HORIZON",
    "WOODROW SHOPE"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "BREAKING AND ENTERING",
      "BRUSH FIRE",
      "DOA",
      "DOMESTIC DISTURBANCE",
      "EMERGENCY RUN",
      "FIGHT IN PROGRESS",
      "FIRE ALARM",
      "INVESTIGATE ---- ?",
      "MESSAGE \\ INFORMATION",
      "MISCELLANEOUS FIRE",
      "MVA WITH INJURY",
      "NON EMERGENCY RUN",
      "POSS SUICIDE",
      "STOPPING SUSPICIOUS VEHICLE",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE",
      "VEHICLE ACCIDENT",
      "VEHICLE FIRE"
  );

  private static final String[] CITY_LIST = new String[]{
    "FRANKLIN", 
    "HIGHLANDS", 
    "OTTO",
    "FRANKLIN TWP", 
    "HIGHLANDS TWP", 
    "SUGARFORK TWP", 
    "BURNINGTOWN TWP",
    "CARTOOGECHAYE TWP", 
    "ELLIJAY TWP", 
    "MILLSHOAL TWP",
    "NANTAHALA TWP", 
    "SMITHBRIDGE TWP",
    
    // Cherokee County
    "TOPTON"
  };
}
