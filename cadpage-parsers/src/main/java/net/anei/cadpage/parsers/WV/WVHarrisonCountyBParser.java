package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.FieldProgramParser;

public class WVHarrisonCountyBParser extends FieldProgramParser {
  
  public WVHarrisonCountyBParser() {
    this("HARRISON COUNTY", "WV");
  }
  
  protected WVHarrisonCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState, 
          "Call_Number:ID! Primary_Incident_Number:SKIP! All_Incident_Numbers:SKIP! ESN:MAP! District:MAP/L! Received:SKIP! Caller:NAME! Call_Taker:SKIP! " +
              "City:CITY! State:ST! ZIP_Code:ZIP! Place:PLACE! Address:ADDR! Apt:APT! Floor:APT! Phone:PHONE! Nature:CALL! Discipline:SKIP! Agency:SKIP! " + 
              "Location:PLACE! Landmark:PLACE! Caller_Address:SKIP! Description:INFO! INFO/N+ ( Location_Alert_Info:ALERT! Units:UNIT! END | TIMES/N+ )");
    
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getAliasCode() {
    return "WVHarrisonCountyB";
  }
  
  @Override
  public String getFilter() {
    return "harrison@911page.net,dispatch@centrale911.com,CADPAGE@CentralE911.local";
  }

  private static final Pattern DELIM = Pattern.compile("\n| +(?=District:|State:|ZIP Code:|Floor:|Phone:|Discipline:|Agency:)");
  private static final Pattern SALEM_UNIT_PTN = Pattern.compile("\\b(SALEM|STA) (\\d{2})\\b");
  private static final Pattern MASTER = Pattern.compile("Call Number: *(\\d+) +(?:Call Received Time: (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d) +)?(.*?)((?: (?:[A-Z]+\\d+|\\d{3,4}|[A-Z]{2,}FD|DOHPUB|AIREVAC|DOH|RCSO))+)");
  private static final Pattern MBLANK_PTN = Pattern.compile("  +");
  private static final Pattern COUNTY_ABRV_PTN = Pattern.compile("(.*) (?:DODD|HARR|RITC)");
  
  private String times;
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    
    body = SALEM_UNIT_PTN.matcher(body).replaceAll("$1$2");
    
    if (body.contains("\n")) {
      times = "";
      if (!parseFields(DELIM.split(body), data)) return false;
      if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n\n", data.strSupp);
      return true;
    }

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    setFieldList("ID DATE TIME CALL ADDR APT CITY UNIT");
    
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
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Unit:") || field.startsWith("Event Type:")) return false;
      parse(field, data);
      return true;
    }
  }
  
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.startsWith("Event Type:")) {
        data.strSupp = append(data.strSupp, "\n", field);
      } else if (field.startsWith("Unit List ")) {
        data.strUnit = field.substring(10).trim();
      } else if (field.startsWith("Unit:")) {
        data.msgType = MsgType.RUN_REPORT;
        times = append(times, "\n\n", field);
      } else {
        times = append(times, "\n", field);
      }
    }
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
      "911 HANG UP",
      "ABDOMINAL /BACK PAIN",
      "ASSIST OTHER AGENCY/OFFICER",
      "ATV/FARM VEH ACCIDENT",
      "AUTO/MVA NO INJURIES",
      "AUTO/MVA WITH ENTRAPMENT",
      "AUTO/MVA WITH INJURIES",
      "AUTO/PEDESTRIAN ACCIDENT",
      "BREATHING DIFFICULTY",
      "BRUSH FIRE",
      "CARDIAC ARREST",
      "CHEST PAINS",
      "CO INVESTIGATION W/ PATIENTS",
      "CO INVESTIGATION W/NO PATIENTS",
      "CO INVESTIGATION",
      "CONTROLLED BURN",
      "DIABETIC EMERGENCY",
      "DISABLED VEH/SIG 20",
      "DOA/DOS/UNATTENDED DEATH",
      "ELECTRICAL FIRE IN STRUCTURE/EXPOSURE",
      "ELECTRICAL FIRE OUTSIDE STRUCTURE",
      "ELECTRICAL FIRE OUTSIDE STRUCTURE",
      "ELECTRICAL FIRE",
      "EXPLOSION/SIG 82",
      "FALLS/ACCIDENTS",
      "FIRE ALARM INVESTIGATION",
      "FLOODING",
      "FOLLOW UP INVESTIGATION",
      "GENERAL OES",
      "HAZ MATERIALS LEAK/SPILL",
      "HEAD INJURY",
      "HEMORRHAGE/BLEEDING",
      "MISSING JUVENILE/SIG 61A",
      "LOCK OUT",
      "NATURAL GAS/OIL LEAK",
      "NON EMERGENCY",
      "NOSE BLEED",
      "OBSTETRICS/BIRTH",
      "ODOR INVESTIGATION",
      "OIL OR GAS FIRE",
      "OVERDOSE",
      "PERSONAL INJURY",
      "POWER LINES DOWN",
      "POWER LINES",
      "PUBLIC SERVICE",
      "ROADWAY OBSTRUCTION",
      "ROADWAY  OBSTRUCTION",
      "ROADWAY",
      "SECURITY ALARM INVESTIGATION",
      "SEIZURES",
      "SICK/UNKNOWN",
      "SMOKE INVESTIGATION",
      "STANDBY",
      "STROKE/CVA",
      "STRUCTURE COLLAPSE",
      "STRUCTURE FIRE - REKINDLE",
      "STRUCTURE FIRE",
      "TEST/TRAINING",
      "TRANSPORTATION ACCIDENT RT 50 W/ENTRAPMENT",
      "TRANSPORTATION ACCIDENT RT 50 W/FATALITY",
      "TRANSPORTATION ACCIDENT RT 50 W/INJURIES",
      "TRANSPORTATION ACCIDENT RT 50 NO INJURI",
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
