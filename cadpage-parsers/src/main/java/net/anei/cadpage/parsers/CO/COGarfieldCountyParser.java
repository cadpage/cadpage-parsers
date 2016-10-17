package net.anei.cadpage.parsers.CO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;

public class COGarfieldCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("(.*) (\\d{4}[- ]\\d{8})\\b(.*)");
  
  public COGarfieldCountyParser() {
    super(CITY_CODES, "GARFIELD COUNTY", "CO");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "GCECA@call3n.com,@everbridge.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Page") && !subject.equals("EverBridge CAD Page")) return false;
    
    // We are processing two formats.  There is always a call ID.
    // In the new format, the Call ID separates the address/apt/city from the Call/Place/Cross
    // In the old format, the call ID is always at the end
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    data.strCallId = match.group(2);
    String tail = match.group(3).trim();
    
    // If we found anything following the call ID, this is the new format
    if (tail.length() > 0) {
      
      setFieldList("ADDR APT CITY ID MAP CALL PLACE X");
      
      // Smart parser can handle the address/apt/city portion
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_RECHECK_APT | FLAG_ANCHOR_END, body, data);
      
      // And the Call/Place/Cross portion
      // It needs a bit of help pulling the call field out.  We have to check the
      // call list ourselves because the START_CALL_PLACE logic expects it to be
      // upshifted.
      StartType st;
      int flags = FLAG_ONLY_CROSS;
      String call = FWD_CALL_LIST.getCode(tail, true);
      if (call != null) {
        st = StartType.START_PLACE;
        data.strCall = call;
        tail = tail.substring(call.length()).trim();
      } else {
        st = StartType.START_CALL;
        flags |= FLAG_START_FLD_REQ;
      }
      parseAddress(st, flags, tail, data);
      
      // City may be a map code
      if (MAP_CODES.contains(data.strCity.toUpperCase())) {
        data.strMap = data.strCity;
        data.strCity = "";
      }
      
      // The cross street information really does go all the way to the end, but it has lots of qualifiers and
      // comma terms that makeit hard to recognize as an address.  So we will parse up to the first address
      // looking thing and append anything else we find to it.
      tail = getLeft();
      String connect = (isCommaLeft() ? ", " : " ");
      data.strCross = append(data.strCross, connect, tail);
      
      // An unrecognized cross street may end up in the place field
      if (data.strPlace.endsWith("/")) {
        data.strCross = append(data.strPlace, " ", data.strCross);
        data.strPlace = "";
      } else if (isValidAddress(data.strPlace)) {
        data.strCross = append(data.strPlace, ", ", data.strCross);
        data.strPlace = "";
      }
    }
    
    // Otherwise it is the old format
    else {
      setFieldList("ADDR APT PLACE MAP CITY X CALL ID");
      
      // The last thing in the line now is the call description.
      // See if we can pick it out with our call list
      String call = REV_CALL_LIST.getCode(body, true);
      if (call != null) {
        data.strCall = call;
        body = body.substring(0,body.length()-call.length()).trim();
      }
      
      // We have to count on the SAP to figure everything else out
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_PAD_FIELD | FLAG_CROSS_FOLLOWS, body, data);
      
      // There is almost always one or two city codes, which makes things easier
      // But occasionally there is no city and we have to deal with that
      if (data.strCity.length() == 0) {
        String left = getLeft();
        if (data.strCall.length() > 0) {
          data.strPlace = left;
        } else {
          data.strCall = left;
          if (data.strCall.length() == 0) return false;
        }
        return true;
      }
  
      // We do have a city code
      // See if it is really a map code
      if (MAP_CODES.contains(data.strCity.toUpperCase())) {
        data.strMap = data.strCity;
        data.strCity = "";
      }
      
      // Any pad field is a place name, or possibly an apt
      String place = getPadField();
      if (place.length() > 0) {
        if (place.length() <= 2) {
          data.strApt = append(data.strApt, "-", place);
        } else {
          data.strPlace = place;
        }
      }
      
      body = getLeft();
      
      int flags = FLAG_ONLY_CROSS | FLAG_ONLY_CITY;
      if (data.strCall.length() > 0) {
        flags |= FLAG_ANCHOR_END;
      } else {
        if (body.length() == 0) return false;
      }
      if (body.length() > 0) {
        Result res = parseAddress(StartType.START_ADDR, flags, body);
        if (data.strCall.length() > 0 || res.getCity().length() > 0) {
          String saveCity = data.strCity;
          res.getData(data);
          if (MAP_CODES.contains(data.strCity.toUpperCase())) {
            data.strMap = data.strCity;
            data.strCity = saveCity;
          }
          body = res.getLeft();
        }
        if (data.strCall.length() == 0) data.strCall = body;
      }
    }
    return true;
  }
  
  @Override
  public CodeSet getCallList() {
    return REV_CALL_LIST;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BAKER HILL",
    "BENT CREEK",
    "BLACK SULPHUR",
    "CASTLE VALLEY",
    "CHAIR BAR",
    "COAL MINE",
    "D J",
    "EAGLES NEST",
    "EL DIENTE",
    "FOREST SERVICE",
    "GRAND VALLEY",
    "GREEN MESA",
    "HIDDEN VALLEY",
    "LITTLE ECHO",
    "MEADOW CREEK",
    "MEL RAY",
    "MINERAL SPRINGS",
    "MORNING STAR",
    "MOUNT SOPRIS",
    "QUEEN CITY",
    "RED MOUNTAIN",
    "RIVER FRONTAGE",
    "RIVER VIEW",
    "SILVER OAK",
    "SOCCER FIELD",
    "SPRING WAGON",
    "ST JOHN",
    "STONE QUARRY",
    "STORM KING",
    "WILLOW CREEK",
    "WILLOW VIEW"
  };

  private static final String[] CALL_LIST = new String[]{
    "Accident",
    "Alarm",
    "Assault",
    "AsstCit",
    "AsstOA",
    "Code",
    "Fraud Forgery",
    "Medical Call",
    "Mutual Aid Request",
    "Park",
    "Shots Fired",
    "Suicidal Subject",
    "Traffic Complaint",
    "Transient",
    "Transport",
    "Warrant",
    "Welfare Check",
    
    "EAbdominal",
    "EAllergic Reaction",
    "EAnimal Bites",
    "EAssault",
    "EBack Pain",
    "EBleeding Non traumatic",
    "EBleeding/Non traumatic",
    "EBleeding/Non-traumatic",
    "EBreathing Difficulty",
    "EBurns",
    "EChest Pain",
    "EChoking",
    "ECPR",
    "EDiabetic",
    "EEnvironmental Emergencies",
    "EEye Problems Injury",
    "EEye Problems/Injury",
    "EFalls",
    "EGynecology Childbirth",
    "EGynecology/Childbirth",
    "EHeadache",
    "EMedical Alarm",
    "EMental Emotional Psych",
    "EMental/Emotional/Psych",
    "ENeurological/Head Injuries",
    "EOverdose Poisoning",
    "EOverdose/Poisoning",
    "ESeizures",
    "ESick Unknown",
    "ESick/Unknown",
    "EStabbing Gunshot",
    "EStabbing/Gunshot",
    "EStroke",
    "ETrauma with Injury",
    "EUnconscious Syncope",
    "EUnconscious/Syncope",
    "EUnresponsive",
    
    "FAlarm",
    "FBrush",
    "FGas Leak",
    "FElevator Alarm",
    "FFuel Leak",
    "FGas Well Fire",
    "FHazMat Incident",
    "FOdor Check",
    "FPower Pole",
    "FRescue Assignment",
    "FRiver Rescue",
    "FSmoke Check",
    "FStructure",
    "FVehicle Fire"
  };
  private static final CodeSet FWD_CALL_LIST = new CodeSet(CALL_LIST);
  private static final CodeSet REV_CALL_LIST = new ReverseCodeSet(CALL_LIST);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CARB", "CARBONDALE",
      "GS",   "GLENWOOD SPRINGS",
      "NC",   "NEW CASTLE",
      "PARA", "PARACHUTE",
      "RIF",  "RIFLE",
      "SIL",  "SILT",
      "SILT", "SILT",
      "Z1",   "Z1",
      "Z2",   "Z2",
      "Z3",   "Z3"
  });
  
  private static final Set<String> MAP_CODES = new HashSet<String>(Arrays.asList(
      "Z1",
      "Z2",
      "Z3"
  ));
}
