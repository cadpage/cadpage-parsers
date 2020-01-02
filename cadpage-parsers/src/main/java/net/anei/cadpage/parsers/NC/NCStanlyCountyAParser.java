package net.anei.cadpage.parsers.NC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCStanlyCountyAParser extends DispatchOSSIParser {
  
  private List<String> addressList = new ArrayList<String>();
  
  public NCStanlyCountyAParser() {
    super(CITY_CODES, "STANLY COUNTY", "NC",
           "FYI? CALL ( SELECT/1 ADDR1/Z+? CITY! | CALL2+? ADDR! ADDR2? ) ( X | PLACE X | ) X+? INFO+");
    setupCityValues(CITY_CODES);
    setDelimiter('/');
    addRoadSuffixTerms("CONNECTOR");
  }
  
  @Override
  public String getFilter() {
    return "CAD@sclg.gov,CAD@stanlycountync.gov";
  }
  
  private static final Pattern BAD_MSG_PTN = Pattern.compile("CAD:BE ADVISED.*|.*\\bSCC/ ?[A-Z]+|.*\\bauth / \\S+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
  
  @Override
  public boolean parseMsg(String body, Data data) {
    boolean good = body.startsWith("CAD:");
    if (!good) body = "CAD:" + body;
    addressList.clear();
    if (body.contains("; ")) body = body.replace(';', '/');
    setSelectValue("1");
    if (super.parseMsg(body, data)) return true;
    
    // Primary parse algorithm only works if there is a city field
    // which occasionally there is not in which case try the secondary 
    // parse algorithm
    if (!good) return false;
    if (BAD_MSG_PTN.matcher(body).matches()) return false;
    setSelectValue("2");
    data.strCall = "";
    return super.parseMsg(body,  data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  // Things get complicated here, the address field just accumulates fields
  // until we find a city field
  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String fld, Data data) {
      addressList.add(fld);
    }
  }
  
  // The city list is where we decide what to do with the address fields
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String fld, Data data) {
      if (!super.checkParse(fld, data)) return false;
      
      // OK, we found address, now figure out what to do with the address
      // If there are more than one address field and the last one is a simple
      // naked road, then merge the last two fields together to form the
      // address field.  If not, the last field is the only address field
      int addrNdx = addressList.size()-1;
      if (addrNdx < 0) abort();
      String sAddr = addressList.get(addrNdx);
      if (addrNdx > 0 && isStreetName(sAddr, true)) {
        sAddr = addressList.get(--addrNdx) + " & " + sAddr;
      }
      parseAddress(sAddr, data);
      
      // Any fields in front of the address field will be appended to the
      // call description
      for (int ii = 0; ii<addrNdx; ii++) {
        data.strCall = append(data.strCall, "/", addressList.get(ii));
      }
      return true;
    }
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CALL_SET.contains(field)) return false;
      data.strCall = append(data.strCall, "/", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isStreetName(field, true)) return false;
      data.strAddress = append(data.strAddress, " & ", field);
      return true;
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isStreetName(field, false)) return false;
      parse(field, data);
      return true;
    }
  }
  
  private boolean isStreetName(String field, boolean strict) {
    int stat = checkAddress(field);
    return  stat == STATUS_STREET_NAME ||
            !strict && stat == STATUS_INTERSECTION || 
            field.contains("BUSINESS 52") ||
            field.equals("COUNTY LINE");
  }
  
  private static final Set<String> CALL_SET = new HashSet<String>(Arrays.asList(new String[]{
      "14-FOR INFORMATION ONLY",
      "21-CALL ( ) AT PHONE NUMBER",
      "25-MEET SUBJECT",
      "27 - DRIVERS LICENCE LOGGED",
      "28 - LICENSE PLATE LOGGED",
      "31-PICKUP",
      "35-DEAD ANIMAL IN THE ROAD",
      "36-HOSTAGE SITUATION",
      "38 - REQUEST K-9",
      "40-FIGHT IN PROGRESS",
      "43-CHASE/PURSUIT",
      "44-RIOT",
      "45 - BOMB COURTHSE",
      "45-BOMB THREAT",
      "46 FALSE",
      "46-ALARM",
      "46T-TESTING ALARM",
      "49-DRAG RACING",
      "50PD TRAFFIC ACCIDENT",
      "53-ROAD BLOCKED",
      "54-HIT & RUN PROPERTY DAMAGE",
      "55-INTOXICATED DRIVER",
      "56-DRUNK PEDESTRIAN",
      "57 - INTOX TESTING/OPERATION",
      "58-DIRECT TRAFFIC",
      "59-ESCORT/CONVOY",
      "60-SUSPICIOUS VEHICLE",
      "61-TRAFFIC STOP",
      "62-BREAKING & ENTERING",
      "63-INVESTIGATION",
      "64-CRIME IN PROGRESS",
      "65-ARMED ROBBERY",
      "67-INVESTIGATE DEATH",
      "68-LIVESTOCK IN ROADWAY",
      "70-IMPROPERLY PARKED VEHICLE",
      "72-PRISONER IN CUSTODY",
      "73-MENTAL PERSON",
      "74-PRISON/JAIL BREAK",
      "75-INDICATES WANTED OR STOLEN",
      "76-PROWLER",
      "77-ASSIST FIRE DEPT W/TRAFFIC",
      "80-BRUSH ALARM",
      "80-BURNING PERMIT",
      "80-CONTROLLED BURN",
      "80-DWELLING ALARM",
      "80-FALSE",
      "80-HAZMAT ALARM",
      "80-INVESTIGATION",
      "80-LOCAL ALARM",
      "80-STRUCTURE ALARM",
      "80-TESTING FIRE ALARM",
      "80-WATER RESCUE",
      "82-GAMBLING INVESTIGATION",
      "83-MISSING PERSON",
      "84-TRAFFIC LIGHT MALFUCTION-84",
      "85-PROPERTY DAMAGE",
      "86-LARCENY",
      "87-STOLEN VEHICLE",
      "88-SUSPICIOUS PERSON",
      "89-RECKLESS/SPEEDING DRIVER",
      "90-ASSAULT",
      "91-DOMESTIC TROUBLE",
      "92-ASSAULT W/DEADLY WEAPON",
      "93-DISTURBANCE",
      "94-PERSON W/GUN",
      "95-SEXUAL ASSAULT",
      "96-ASSIST MOTORIST",
      "97-ANIMAL COMPLAINT",
      "98-EMERGENCY ROAD REPAIR",
      "99-WARRANT/PAPER SERVICE",
      "ABDOMINAL PAINS",
      "AIRCRAFT EMERGENCY",
      "AMHURST DETAIL",
      "ANIMAL CONTROL NOTIFICATION",
      "ASSIST EMS/FIRE",
      "ATEMPT TO LOCATE",
      "ATTACKS",
      "BANNED FROM LOCATION",
      "BOAT FIRE",
      "BOMB THREAT AT SCHOOL",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "CARBON MONOXIDE ALARM",
      "CFND SPC/STRCT COLLAPSE",
      "CHILDBIRTH",
      "CITIZEN ASSIST/SERVICE CALL",
      "CODE RED - PLANE DOWN",
      "CODE STEMI",
      "CODE YELLOW-PLANE IN DISTRESS",
      "COLD EXPOSURE",
      "COMMERCIAL STRUCTURE FIRE",
      "COMMUNITY PARAMEDIC VISIT",
      "CONVULSION",
      "CRIME STOPPERS REPORT",
      "CVA",
      "DAM EMERGENCY",
      "DAMAGE RELATED TO STORM",
      "DIVING ACCIDENT",
      "DOT NOTIFICATION",
      "DSS NOTIFICATION",
      "E1-ABDOMINAL PAINS/PROBLEMS",
      "E10-CHEST PAIN",
      "E11-CHOKING",
      "E12-SEIZURES/CONVULSION",
      "E13-DIABETIC PROBLEMS",
      "E14-DROWNING/DIVING ACCIDENT",
      "E15-ELECTROCUTION",
      "E16-EYE PROBLEMS/INJURY",
      "E17-FALL WITH INJURY",
      "E18-HEADACHE",
      "E19-HEART PROBLEMS",
      "E2-ALLERG/HIVES/MED REACT/STIN",
      "E20-HEAT/COLD EXPOSURE",
      "E21-HEMORRHAGE/LACERATIONS",
      "E22-INDUSTRIAL/MACHINE ACCIDEN",
      "E23-OVERDOSE/INGESTION/POISONI",
      "E24-PREGNANCY/CHILDBIRTH/MISCA",
      "E25-PSYCHIATIC/SUICIDE ATTEMPT",
      "E26-SICK PERSON",
      "E27-STAB/GUNSHOT WOUND",
      "E28-STROKE/CVA",
      "E29 - ATV",
      "E29 - VEH VS BICYCLE/MOTORCYCL",
      "E29-10-50 PI",
      "E3-ANIMAL BITES/ATTACKS",
      "E30-TRAUMATIC INJURIES",
      "E31-UNCONSCIOUS/FAINTING",
      "E32-UNKNOWN/PROBLEM/MAN DOWN",
      "E33-TRANSFER/INTERFACILITY",
      "E36-PANDEMIC/EIDEMIC/OUTBREAK",
      "E37-INTERFACILITY/TRANSFER",
      "E4-ASSAULT/RAPE",
      "E5-BACK PAIN (NON-TRAMATIC)",
      "E6-BREATHING PROBLEMS",
      "E7-BURNS/EXPLOSION",
      "E8-CARBON MON/INHALATION/HAZMA",
      "E9-CARDIAC/RESPIRATORY ARREST",
      "ELECTRICAL HAZARD",
      "ELEVATOR RESCUE",
      "EMD",
      "EMERG RADIO NOTIFICATION",
      "EMERGENCY MEDICAL",
      "EMERGENCY TRANSFER",
      "EXISTING CONDITIONS/ROADWAY",
      "EXPLOSION",
      "EXTRICATION/ENTRAPPED",
      "FAINTING",
      "FIRE ALARM - COMMERICAL",
      "FIRE ALARM - RESID",
      "FUEL SPILL",
      "GAS LEAK/ODOR",
      "GENERATOR TEST COURTHOUSE",
      "GENERATOR TEST TOWER",
      "GUNSHOT WOUND",
      "HAZMAT",
      "HHC NOTIFICATION",
      "HIGH ANGLE RESCUE",
      "HIVES",
      "INGESTION",
      "LACERATIONS",
      "LANDING ZONE",
      "LIFESAVER SEARCH",
      "LIGHTNING STRIKE",
      "MACHINE ACCIDEN",
      "MAJOR/SPECIAL INCIDENT",
      "MAN DOWN",
      "MARINE FIRE",
      "MED REACT",
      "MEDICAL CALL",
      "MISCA",
      "MISDIALED 911",
      "MOTORCYCL",
      "MUTUAL AID / ASSIST",
      "MUTUAL AIDE",
      "MVA WITH PIN IN",
      "NWS WATCH OR WARNING",
      "ODOR (STRANGE/UNKNOWN)",
      "OFFICER NEEDS ASSISTANCE",
      "OUT OF COUNTY EMS ASSIST",
      "OUTSIDE FIRE",
      "OUTSIDE SMOKE INVESTIGATION",
      "PARKING DETAIL",
      "PHONE COMPUTER RESTARTS",
      "POISONI",
      "POWER OUTAGE",
      "PROBLEM",
      "PROBLEMS",
      "PUBLIC WORKS NOTIFICATION",
      "RADIO STUDY PROJECT",
      "RAPE",
      "RECALL OFF DUTY EMS",
      "RECOVERY",
      "REPAIR OF EQUIP",
      "REPORT TO HEADQUARTERS",
      "REPOSSESSION OF VEHICLE",
      "RESPIRATORY ARREST",
      "ROUTINE MEDICAL",
      "ROUTINE TRANSFER",
      "RUOK CHECK",
      "S15-EXPLOSION",
      "S16-ABDUCTION/KIDNAPPING",
      "S18-HAZ-MAT INCIDENT",
      "S19-AIRPLANE CRASH",
      "S2-CONTROLLED SUBSTANCE",
      "S20-MURDER",
      "S23-FUNERAL",
      "S36-OFFICER HELD HOSTAGE",
      "S80-BIO-HAZARD",
      "S911-911 HANG-UP",
      "SCHOOL PANIC ALARM ACTIVATION",
      "SEARCH OR RESCUE",
      "SECURITY CHECK/PREMISE",
      "SIGNAL 61-LICENSE CHECKING STA",
      "SPECIAL DETAIL",
      "STANDBY",
      "STIN",
      "STRUCTURE FIRE",
      "SUICIDE ATTEMPT",
      "SUICIDE THREAT",
      "TRAIN/RAIL INCIDENT",
      "TRAINING DETAIL",
      "TRANSPORT",
      "UTILITY CO. NOTIFICATION",
      "VEHICLE FIRE",
      "WATER RESCUE",
      "WATERCRAFT IN DISTRESS"
}));
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALB",  "ALBEMARLE",
      "BAD",  "BADIN",
      "GLH",  "GOLD HILL",
      "LOC",  "LOCUST",
      "MID",  "MIDLAND",
      "MIS",  "MISENHEIMER",
      "MTP",  "MT PLEASANT",
      "NEW",  "NEW LONDON",
      "NOR",  "NORWOOD",
      "OAK",  "OAKBORO",
      "RFD",  "RICHFIELD",
      "SFD",  "STANFIELD"
  });
}
