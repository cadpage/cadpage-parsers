
package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCAsheCountyParser extends DispatchSouthernParser {

  public NCAsheCountyParser() {
    super(CITY_LIST, "ASHE COUNTY", "NC", 
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE|DSFLG_OPT_CODE|DSFLG_ID|DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("VIRGINIA ST LINE");
    setupSaintNames("MARYS");
    setupDoctorNames("CLAY");
    addExtendedDirections();
    removeWords("ALLEY", "CIRCLE", "PARKWAY", "PLACE", "RUN", "SQUARE", "TERRACE");
  }
  
  @Override
  public String getFilter() {
    return "@washconc.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(?:(\\S+) - +)?(.*?)[-/ ]*");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Clean up call
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) { // Always matches
      data.strCode = getOptGroup(match.group(1));
      data.strCall = match.group(2);
    }
    
    // Fix mispelled city names
    data.strCity = convertCodes(data.strCity.toUpperCase(), FIX_CITY_TABLE);
    
    // Check for out of state locations
    String state = CITY_ST_TABLE.getProperty(data.strCity);
    if (state != null) data.strState = state;
    return true;
  }
  
  @Override
  public String getProgram() {
    return "X " + super.getProgram().replace("CITY", "CITY ST");
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("(") && apt.endsWith(")")) return true;
    if (apt.length() > 9) return true;
    if (apt.equals("RD")) return true;
    return super.isNotExtraApt(apt);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    int pt = addr.indexOf('&');
    if (pt >= 0) pt = addr.indexOf('&', pt+1);
    boolean extraAmp = (pt >= 0);
    addr = super.adjustMapAddress(addr);
    if (extraAmp) {
      pt = addr.lastIndexOf('&');
      if (pt >= 0) addr = addr.substring(0, pt).trim();
    }
    return addr;
  }

  // Only need to keep track of street names with 4 or more words
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ASHE COUNTY HIGH SCHOOL",
    "EAST DOUBLE SPRINGS CH",
    "EAST LITTLE HORSE CREEK",
    "EAST MT VIEW LODGE",
    "MT JEFFERSON STATE PARK",
    "NORTH FORK NEW RIVER",
    "SOUTH BIG HORSE CREEK",
    "SOUTH J E GENTRY",
  };
  
  private static final Properties CITY_ST_TABLE = buildCodeTable(new String[]{
      "GRAYSON COUNTY",    "VA",
      "GRAYSON",           "VA",
      "JOHNSON COUNTY",    "TN",
      "JOHNSON",           "TN",
      "MOUNTAIN CITY",     "TN"
  });

  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "CRUMLPER",   "CRUMPLER"
  });

  private static final String[] CITY_LIST = new String[]{
    
      "ASHE COUNTY",
      "ASHE",
      
      //Towns
      "JEFFERSON",
      "LANSING",
      "WEST JEFFERSON",

      //Townships
      "CHESTNUT HILL",
      "CLIFTON",
      "CRESTON",
      "ELK",
      "GRASSY CREEK",
      "HELTON",
      "HORSE CREEK",
      "HURRICANE",
      "JEFFERSON",
      "LAUREL",
      "LAUREL SPRINGS",
      "NORTH FORK",
      "OBIDS",
      "OLD FIELDS",
      "PEAK CREEK",
      "PINE SWAMP",
      "PINEY CREEK",
      "POND MOUNTAIN",
      "WALNUT HILL",
      "WEST JEFFERSON",

      //Unincorporated Communities
      "BEAVER CREEK",
      "BINA",
      "CHESTNUT HILL",
      "CLIFTON",
      "COMET",
      "CRESTON",
      "CRUMPLER",
      "CRUMLPER",  // Misspelled
      "FIG",
      "FLEETWOOD",
      "GLENDALE SPRINGS",
      "GRASSY CREEK",
      "GRAYSON",
      "HELTON",
      "PARKER",
      "SCOTTVILLE",
      "STURGILLS",
      "TODD",
      "WARRENSVILLE",
      
      // Alleghany County
      "ALLEGHANY COUNTY",
      "ALLEGHANY",
      "CRANBERRY",
      "ENNICE",
      "PINEY CREEK",
      "PRATHERS CREEK",
      "SPARTA",
      
      // Wataugua County
      "WATAUGUA COUNTY",
      "WATAUGUA",
      "BALD MOUNTAIN",
      "BOONE",
      "DEEP GAP",
      "NORTH FORK",
      "STONY FORK",
      "ZIONVILLE",
      
      // Wilkes County
      "WILKES COUNTY",
      "WILKES",
      "ELK",
      "JOBS CABIN",
      "UNION",
      
      // Other counties
      
      "GRAYSON COUNTY",
      "GRAYSON",
      
      "JOHNSON COUNTY",
      "JOHNSON",
      "MOUNTAIN CITY",
      
      // Washington County is on the other side of the state
      // But someone they have one department there (Creswell VFD)
      "WASHINGTON COUNTY",
      "WASHINGTON",

      // Cities
      "CRESWELL",
      "PLYMOUTH",
      "ROPER",

      // Unincorporated communities
      "LAKE PHELPS",
      "MACKEYS",
      "PEA RIDGE",
      "PLEASANT GROVE",

      // Townships
      "PLYMOUTH",
      "LEES MILL",
      "SCUPPERNONG",
      "SKINNERSVILLE"
  };
}
