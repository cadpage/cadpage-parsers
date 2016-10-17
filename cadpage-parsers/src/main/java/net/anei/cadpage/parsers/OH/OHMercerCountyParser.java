package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser.Result;
import net.anei.cadpage.parsers.SmartAddressParser.StartType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHMercerCountyParser extends DispatchEmergitechParser {
  
  public OHMercerCountyParser() {
    super(true, CITY_LIST, "MERCER COUNTY", "OH");
    setupSpecialStreets("ERASTUS DURBIN");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern DIR_OF_PTN = Pattern.compile("\\b([NSEW])/OF\\b");
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" BTWN ", " BETWEEN ");
    body = DIR_OF_PTN.matcher(body).replaceAll("$1O");
    if (!super.parseMsg(body, data)) return false;
    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, CALL_CODES);
    data.strCross = data.strCross.replace(" BETWEEN: ", " BTWN ");
    data.strSupp = data.strSupp.replace(" BETWEEN: ", " BTWN ");
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  @Override
  protected Result parseAddress(StartType sType, int flags, String address) {
    if (sType == StartType.START_PLACE) sType = StartType.START_ADDR;
    return super.parseAddress(sType, flags, address);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("MRCR V WRT CO LN", "MERCER VANWERT COUNTY LINE");
    addr = addr.replace("MRCR VW CO LN", "MERCER VANWERT COUNTY LINE");
    addr = addr.replace("VAN WERT MRC CO LN", "MERCER VANWERT COUNTY LINE");
    addr = addr.replace("MRCR DRKE CO LN", "DARKE-MERCER COUNTY LINE");
    addr = addr.replace("OHIO IND STATE LN", "INDIANA OHIO LINE");
    addr = addr.replace("IND OHIO STATE LN", "INDIANA OHIO LINE");
    return addr;
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "02",   "Accident/Property",
      "04",   "Injury Accident",
      "06",   "Airplane Crash",
      "08",   "Assault",
      "09",   "Investigate Complaint",
      "10",   "Assist Another Unit",
      "100",  "911 Hang Up",
      "101",  "911 Non Emergency",
      "102",  "911 Pocket Call",
      "10M",  "Motorist Assist / DMV",
      "110",  "Log Information",
      "12",   "Burglary",
      "12A",  "Burglary In Progress",
      "13",   "Special Detail",
      "13B",  "Bank Detail",
      "13D",  "K-9 Demonstration",
      "13E",  "Extra Patrol",
      "13F",  "Funeral Details",
      "13H",  "House/Business Check",
      "13K",  "K-9 Call Out",
      "13L",  "Lock Out / Vehicle / Resident",
      "13M",  "Message Delivery",
      "13S",  "Sex Offender Verifications",
      "13T",  "School / Training / Meeting",
      "14",   "Bad Check",
      "15",   "Paper Service",
      "15A",  "Warrant Service",
      "15D",  "Deliver Paper",
      "15F",  "Filing/Signing Charges",
      "15I",  "Civil Issue / Report Requested",
      "15S",  "Civil Sale",
      "15V",  "Court Order / Bond Violation",
      "16",   "DOA / Death Investigation",
      "17",   "Contact Party in Person",
      "18",   "Animal Bite",
      "18A",  "Animal Abuse / Neglect",
      "18B",  "Animal Barking",
      "18L",  "Animal Lost / Found",
      "18R",  "Animal Report / Non Abuse Neglect",
      "19",   "Contact Party by Phone",
      "20",   "Domestic",
      "20D",  "Domestic Dispute",
      "21",   "Prisoner Transport",
      "210",  "Shift Log",
      "21B",  "Bond",
      "21C",  "Out at Court",
      "22",   "Drowning",
      "24",   "Drunk",
      "24F",  "Underage FI Card",
      "24J",  "Underage Drinking Party",
      "24O",  "OVI Report",
      "26",   "Fight",
      "28",   "Fire",
      "28A",  "Alarm/Fire",
      "28C",  "Alarm/Carbon Monoxide",
      "28G",  "Fire/Grass/Field",
      "28K",  "Knox Box Release",
      "28L",  "Gas Leak/Spill",
      "29",   "Squad Run",
      "29A",  "Air Ambulance / Helicopter",
      "29M",  "Medical Assist",
      "2A",   "Accident/Animal",
      "2D",   "Accident/Deer",
      "2P",   "Crash On Private Property",
      "2S",   "Accident Slide Off",
      "32",   "Homicide",
      "34",   "Juvenile",
      "34A",  "Child Abuse / Neglect",
      "34U",  "Unruly Juvenile",
      "36",   "Theft / Larceny",
      "36C",  "Counterfeit / Forgery",
      "36D",  "Drive Off",
      "36S",  "Scam / Fraud Reports",
      "38",   "Missing Adult",
      "38J",  "Missing Juv",
      "38K",  "Kidnapping/Abduction",
      "40",   "Man With A Gun",
      "40A",  "Man With A Knife",
      "42",   "Nature Unknown",
      "44",   "Officer In Trouble",
      "46",   "Prowler",
      "48",   "Rape/Sex Crimes",
      "4A",   "Injury Accident/Animal",
      "4D",   "Injury Accident/Deer",
      "4F",   "Fatal Accident",
      "50",   "Robbery",
      "50A",  "Robbery In Progress",
      "52",   "Shooting",
      "52A",  "Shots Fired",
      "54",   "Stabbing/Cutting",
      "56",   "Stolen Vehicle",
      "56U",  "Unauthorized Use Of A Motor Vehicle",
      "58",   "Suicide",
      "58A",  "Suicide Attempt",
      "58T",  "Suicide Threat",
      "60",   "Suspicious Person",
      "60A",  "Suspicious Vehicle",
      "62",   "Traffic Offense",
      "62P",  "Pursuit",
      "64",   "Vandalism",
      "64P",  "Property Damage",
      "66",   "Jail Escape",
      "66A",  "Incident In Jail",
      "66D",  "Inmate Drug Incident",
      "68",   "Drugs",
      "68E",  "Taskforce Evidence",
      "68N",  "Overdose Narcan Used",
      "68O",  "Overdose",
      "68T",  "Drug Test Report",
      "70",   "Mental Subject Call",
      "72",   "Road Block",
      "74",   "Riot",
      "78",   "Open Door",
      "78A",  "Open Window",
      "80",   "Bomb Threat",
      "90",   "Alarm",
      "90A",  "Alarm/Active",
      "90B",  "Alarm/False",
      "90R",  "Alarm/ Portable / School / Court",
      "911A", "Active911 Saturday Test",
      "9A",   "Abandoned Junk Vehicle",
      "9B",   "Be On The Lookout",
      "9C",   "Cellebrite Extraction",
      "9D",   "Disorderly Conduct Issues",
      "9E",   "Erratic Driver",
      "9F",   "Follow Up",
      "9G",   "Discharge Of Firearm",
      "9H",   "Telecommunications Harassment",
      "9I",   "Industrial Accident",
      "9J",   "Junk Yard Inspections",
      "9K",   "Signs / Lights",
      "9L",   "Littering",
      "9M",   "Menacing Report",
      "9N",   "Neighbor Dispute",
      "9R",   "Recovered Property",
      "9S",   "Sex Offender Offenses",
      "9T",   "Trespassing Report",
      "9V",   "Tow / Impound / REPO",
      "9W",   "Welfare Check",
      "9Z",   "Wildlife Complaint",
      "TK",   "Test Call"
  });
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "CELINA",

    // Villages
    "BURKETTSVILLE",
    "CHICKASAW",
    "COLDWATER",
    "FORT RECOVERY",
    "MENDON",
    "MONTEZUMA",
    "ROCKFORD",
    "ST HENRY",

    // Townships
    "BLACK CREEK",
    "BUTLER",
    "CENTER",
    "DUBLIN",
    "FRANKLIN",
    "GIBSON",
    "GRANVILLE",
    "HOPEWELL",
    "JEFFERSON",
    "LIBERTY",
    "MARION",
    "RECOVERY",
    "UNION",
    "WASHINGTON",

    // Unincorporated communities
    "CARTHAGENA",
    "CASSELLA",
    "CHATTANOOGA",
    "CRANBERRY PRAIRIE",
    "DURBIN",
    "ERASTUS",
    "HINTON",
    "MACEDON",
    "MARIA STEIN",
    "MERCER",
    "NEPTUNE",
    "PADUA",
    "PHILOTHEA",
    "SEBASTIAN",
    "SHARPSBURG",
    "SHIVELY",
    "SKEELS CROSSING",
    "SKEELS CROSSROADS",
    "ST JOSEPH",
    "ST PETER",
    "ST ROSE",
    "TAMA",
    "WABASH",
    "WENDELIN",
    
    // Alaglaize County
    "ST MARYS",
    
    // Miami County
    "TROY",
    
    // Wert County
    "VENEDOCIA"
  };
}
