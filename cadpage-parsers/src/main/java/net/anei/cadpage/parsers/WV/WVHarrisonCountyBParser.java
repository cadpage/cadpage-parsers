package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WVHarrisonCountyBParser extends SmartAddressParser {
  
  public WVHarrisonCountyBParser() {
    this("HARRISON COUNTY", "WV");
  }
  
  protected WVHarrisonCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState);
    setFieldList("ID DATE TIME CALL ADDR APT CITY UNIT");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getAliasCode() {
    return "WVHarrisonCountyB";
  }
  
  @Override
  public String getFilter() {
    return "dispatch@centrale911.com";
  }
  
  private static final Pattern SALEM_UNIT_PTN = Pattern.compile("\\b(SALEM|STA) (\\d{2})\\b");
  private static final Pattern MASTER = Pattern.compile("Call Number: *(\\d+) +(?:Call Received Time: (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d) +)?(.*?)((?: (?:[A-Z]+\\d+|\\d{3,4}|[A-Z]{2,}FD|AIREVAC|DOH|RCSO))+)");
  private static final Pattern MBLANK_PTN = Pattern.compile("  +");
  private static final Pattern COUNTY_ABRV_PTN = Pattern.compile("(.*) (?:DODD|HARR|RITC)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    
    body = SALEM_UNIT_PTN.matcher(body).replaceAll("$1$2");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strDate = getOptGroup(match.group(2));
    data.strTime = getOptGroup(match.group(3));
    String addr = match.group(4).trim();
    data.strUnit = match.group(5).trim();
    
    if (addr.startsWith("Call Received Time:")) return false;
    
    addr = MBLANK_PTN.matcher(addr).replaceAll(" ");
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_EMPTY_ADDR_OK | FLAG_ANCHOR_END, addr, data);
    
    match = COUNTY_ABRV_PTN.matcher(data.strAddress);
    if (match.matches()) data.strAddress = match.group(1).trim();
    
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = WV_STATE_PTN.matcher(addr).replaceAll("WV");
    return super.adjustMapAddress(addr);
  }
  
  private static final Pattern WV_STATE_PTN = Pattern.compile("\\bWV STATE\\b", Pattern.CASE_INSENSITIVE);
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BIG BATTEL",
      "BIG BATTLE",
      "BIG FLINT",
      "BONDS CREEK",
      "BRADEN HILL",
      "BROAD RUN",
      "BURTON RUN",
      "CHURCH HILL",
      "ELK LICK",
      "FRANKS RUN",
      "INDIAN CREEK",
      "JOY CABIN RUN",
      "MACELROY CREEK",
      "MCINTYRE FORK",
      "NAZARETH FARM",
      "OIL RIDGE",
      "PENNSBORO INDUSTRIAL PARK",
      "PINE GROVE CHURCH",
      "PLEASANT HILL",
      "RALPHS RUN",
      "SALEM BIG RUN",
      "SILVER RUN",
      "SKELTON RUN",
      "SYCAMORE FORK",
      "UNION RIDGE",
      "WHITE OAK"

  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL /BACK PAIN",
      "ASSIST OTHER AGENCY/OFFICER",
      "ATV/FARM VEH ACCIDENT",
      "AUTO/MVA NO INJURIES",
      "AUTO/MVA WITH ENTRAPMENT",
      "AUTO/MVA WITH INJURIES",
      "BREATHING DIFFICULTY",
      "BRUSH FIRE",
      "CARDIAC ARREST",
      "CHEST PAINS",
      "CO INVESTIGATION W/ PATIENTS",
      "CO INVESTIGATION W/NO PATIENTS",
      "CO INVESTIGATION",
      "CONTROLLED BURN",
      "DISABLED VEH/SIG 20",
      "ELECTRICAL FIRE IN STRUCTURE/EXPOSURE",
      "ELECTRICAL FIRE OUTSIDE STRUCTURE",
      "ELECTRICAL FIRE OUTSIDE STRUCTURE",
      "ELECTRICAL FIRE",
      "EXPLOSION/SIG 82",
      "FIRE ALARM INVESTIGATION",
      "FLOODING",
      "HEAD INJURY",
      "HEMORRHAGE/BLEEDING",
      "MISSING JUVENILE/SIG 61A",
      "NATURAL GAS/OIL LEAK",
      "NON EMERGENCY",
      "OBSTETRICS/BIRTH",
      "ODOR INVESTIGATION",
      "OIL OR GAS FIRE",
      "PERSONAL INJURY",
      "POWER LINES DOWN",
      "POWER LINES",
      "PUBLIC SERVICE",
      "ROADWAY OBSTRUCTION",
      "ROADWAY",
      "SECURITY ALARM INVESTIGATION",
      "SEIZURES",
      "SMOKE INVESTIGATION",
      "STROKE/CVA",
      "STRUCTURE COLLAPSE",
      "STRUCTURE FIRE - REKINDLE",
      "STRUCTURE FIRE",
      "TEST/TRAINING",
      "TRANSPORTATION ACCIDENT/WITH ENTRAPMENT",
      "TRANSPORTATION ACCIDENT/WITH INJURIES",
      "TRANSPORTATION ACCIDENT/WITH NO INJURIES",
      "UNKNOWN TYPE FIRE",
      "UNRESPONSIVE PERSON",
      "VEHICLE FIRE",
      "WELFARE CHECK"
  );
  
  private static final String[] CITY_LIST = new String[]{

    // Harrison County
    // Cities
    "BRIDGEPORT",
    "CLARKSBURG",
    "SALEM",
    "SHINNSTON",
    "STONEWOOD",

    // Towns
    "ANMOORE",
    "LOST CREEK",
    "LUMBERPORT",
    "NUTTER FORT",
    "WEST MILFORD",

    // Census-designated places
    "DESPARD",
    "ENTERPRISE",
    "GYPSY",
    "HEPZIBAH",
    "SPELTER",
    "WOLF SUMMIT",

    // Unincorporated communities
    "ARLINGTON",
    "GLEN FALLS",
    "GYPSY",
    "JIMTOWN",
    "MOUNT CLARE",
    "WALLACE",
    
    
    // Richie County
    // Cities
    "PENNSBORO",

    // Towns
    "AUBURN",
    "CAIRO",
    "ELLENBORO",
    "HARRISVILLE",
    "PULLMAN",

    // Unincorporated communities
    "BEREA",
    "BROHARD",
    "BURNT HOUSE",
    "FONZO",
    "MACFARLAN",
    "PETROLEUM",
    "SMITHVILLE",

    // Counties
    "BARBOUR CO",
    "BARBOUR COUNTY",
    "CALHOUN CO",
    "CALHOUN COUNTY",
    "DODRIDGE CO",
    "DODDRIDGE COUNTY",
    "GILMER",
    "GILMER CO",
    "HARRISON CO",
    "HARRISON COUNTY",
    "LEWIS CO",
    "LEWIS COUNTY",
    "MARION CO",
    "MARION COUNTY",
    "PLEASANTS CO",
    "PLEASANTS COUNTY",
    "RITCHIE CO",
    "RITCHIE COUNTY",
    "TAYLOR CO",
    "TAYLOR COUNTY",
    "TYLER CO",
    "TYLER COUNTY",
    "UPSHUR CO",
    "UPSHUR COUNTY",
    "WETZEL CO",
    "WETZEL COUNTY",
    "WIRT CO",
    "WIRT COUNTY",
    "WOOD CO",
    "WOOD COUNTY",
    
    // Doddridge County
    "ASHLEY",
    "CENTER POINT",
    "NEW MILTON",
    "SMITHBURG",
    "WEST UNION"
  };

}
