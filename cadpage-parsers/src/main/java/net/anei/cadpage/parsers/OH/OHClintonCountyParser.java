package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA10Parser;

/**
 * Clinton County, OH
 */
public class OHClintonCountyParser extends DispatchA10Parser {

  public OHClintonCountyParser() {
    super(CITY_LIST, "CLINTON COUNTY", "OH",
          "( CODE ADDR ( ID | CITY ST_ZIP ID ) INFO! INFO+? X2 CITY END " +
          "| CALL ADDR/S PHONE? INFO! X+ )");
  }
  
  @Override
  public String getFilter() {
    return "noreply@clintonsheriff.com";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }
    
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("PHONE")) return new PhoneField("\\d{7}");
    if (name.equals("ST_ZIP")) return new MyStateZipField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyCodeField extends CodeField {
    public MyCodeField() {
      super("\\d{1,2}(?: ?[A-Z]+)?|IO");
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCall = convertCodes(field, CALL_CODES);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL"; 
    }
  }
  
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyStateZipField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = STATE_ZIP_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strState = match.group(1);
      if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(2));
    }

    @Override
    public String getFieldNames() {
      return "ST CITY";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "1",   "Contact Sheriff",
      "2",   "Accident (prop damage)",
      "3",   "Contact Chief Deputy",
      "4",   "MVA with Possible Injuries",
      "5",   "Contact HQ",
      "6",   "Aircraft Crash",
      "7",   "License Registration (req)",
      "8",   "Assault",
      "9",   "Investigate Complaint",
      "10",  "Assist another unit",
      "11",  "Arrest record Check (req)",
      "12",  "Burglary",
      "12A", "Burglary in Progress",
      "13",  "Special Detail",
      "14",  "Bad checks Forgery",
      "15",  "Civil Process",
      "15f", "Felony",
      "15M", "Misdemeanor",
      "16",  "Dead on arrival",
      "17",  "Contact Party In person",
      "18",  "Dog Bite",
      "18A", "Animal Call",
      "19",  "Contact party by phone",
      "20",  "Domestic Trouble",
      "20A", "Neighbor Trouble",
      "21",  "Prowler",
      "22",  "Drowning",
      "23",  "Off Air to Eat (Location)",
      "24",  "Drunk",
      "25",  "Return to HQ",
      "26",  "Fight",
      "27",  "Emergency Run",
      "28",  "Fire",
      "28A", "Vehicle Fire",
      "28B", "Structural Fire",
      "28C", "Structure Fire Possibly Entrapment",
      "28D", "Explosion",
      "29",  "Emergency Squad",
      "29a", "Air Ambulance",
      "29b", "Funeral Home",
      "2a",  "Hit Skip",
      "30",  "Gambling",
      "31",  "Wrecker Required",
      "32",  "Homicide",
      "33",  "Car in Service",
      "34",  "Juveniles",
      "34A", "Runaway / Unruly",
      "35",  "On Patrol",
      "36",  "Larceny",
      "36A", "Larceny in progress",
      "37",  "Car out of Service (off Air)",
      "38",  "Missing person",
      "39",  "Give your location",
      "40",  "Man with Gun",
      "40A", "Man with Knife",
      "41",  "One unit in radio contact",
      "42",  "Nature Unknown",
      "43",  "Home",
      "44",  "Officer in Trouble",
      "45",  "Contact New media",
      "46",  "Prowlers",
      "47",  "Get Report",
      "48",  "Rape",
      "48a", "Sex offense",
      "48B", "Expose",
      "49",  "Radio Repair",
      "4a",  "Hit Skip",
      "50",  "Robbery",
      "50a", "Robbery in Progress",
      "51",  "Can no Answer",
      "52",  "Shooting",
      "54",  "Stabbing or cutting",
      "55",  "Civil defense alert",
      "56",  "Stolen Car",
      "56A", "Wanted Vehicle",
      "57",  "Escort or Parade",
      "57a", "Money Escort",
      "58",  "Suicide",
      "59",  "Computer down",
      "60",  "Suspicious Person",
      "60A", "Suspicious Car",
      "61",  "Pick up another unit",
      "62",  "Traffic Detail",
      "63",  "Investigation or follow up",
      "64",  "Vandalism",
      "65",  "Request detective at scene",
      "65a", "Evidence Technician",
      "65b", "Hostage Negotiation",
      "65c", "SWAT",
      "65d", "K-9 unit",
      "65e", "Victim advocate",
      "65f", "Coroner",
      "65g", "Prosecutor",
      "66",  "Escape or jail break",
      "67",  "Rush call but no emergency",
      "68",  "Livestock on Roadway",
      "69",  "Narcotics",
      "70",  "Emergency Notification",
      "71",  "Change Channel",
      "72",  "Threats or harassment",
      "74",  "Hazardous spill",
      "76",  "Mental",
      "78",  "Alarm drop",
      "7a",  "Stolen File check (req)",
      "80",  "Traffic jam/road blocked",
      "82",  "Disabled vehicle",
      "84",  "Open Door",
      "84A", "Open Window",
      "85",  "Fuel Stop",
      "86",  "Traffic Offense",
      "86A", "Motorcycle/ATV/Mini Bike",
      "88",  "Bomb Threat",
      "90",  "Train Derailment",
      "91",  "911 hang up - contact in person",
      "99",  "Emergency Traffic (all stand by)",

  });
  
  private static final String[] CITY_LIST = new String[]{
    // City
    "WILMINGTON",

    // Villages
    "BLANCHESTER",
    "CLARKSVILLE",
    "LYNCHBURG",
    "MARTINSVILLE",
    "MIDLAND",
    "NEW VIENNA",
    "PORT WILLIAM",
    "SABINA",

    // Townships
    "ADAMS",
    "CHESTER",
    "CLARK",
    "GREEN",
    "JEFFERSON",
    "LIBERTY",
    "MARION",
    "RICHLAND",
    "UNION",
    "VERNON",
    "WASHINGTON",
    "WAYNE",
    "WILSON",

    // Unicorporated villages
    "BLOOMINGTON",
    "BURTONVILLE",
    "CUBA",
    "FARMERS STATION",
    "GURNEYVILLE",
    "JONESBORO",
    "LEES CREEK",
    "LUMBERTON",
    "MCKAYS STATION",
    "MELVIN",
    "MEMPHIS",
    "MORRISVILLE",
    "NEW ANTIOCH",
    "NORTH KINGMAN",
    "OAKLAND",
    "OGDEN",
    "POWDER LICK",
    "REESVILLE",
    "SLIGO",
    "SOUTH KINGMAN",
    "WALLOPSBURG",
    "WESTBORO",
    
    // In Highland County
    "LEESBURG"

  };
}
