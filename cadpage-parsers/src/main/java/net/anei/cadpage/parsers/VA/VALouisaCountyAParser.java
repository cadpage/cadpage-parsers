package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

public class VALouisaCountyAParser extends FieldProgramParser {
  
  public VALouisaCountyAParser() {
    super(CITY_CODES, "LOUISA COUNTY", "VA",
           "( UNIT BOX2 ADDRCITY APT PLACE CALL ID2! " +
           "| BOX2 ADDRCITY APT PLACE CALL ID2! " +
           "| ID1? CALL! ADDR/S! PLACE BOX1! Info:INFO! )");
    setupCities(CITY_LIST);
  }
  
  @Override
  public String getFilter() {
    return "911@louisa.org,Dispatch@louisa.org,CADEMS2@louisa.org,CADEMS4@louisa.org";
  }
  
  private static final Pattern I_ADDR_PTN = Pattern.compile(".*\\bI *\\d+\\b.*");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Notification")) return false;
    int pt = body.indexOf(" Closed APCO Case:");
    if (pt < 0) pt = body.indexOf(" Opened APCO Case:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    String[] flds = body.split(";");
    if (flds.length >= 5) {
      if (!parseFields(flds, data)) return false;
    }
    
    else {
    flds = body.split(":");
      if (flds.length >= 7) {
        if (!parseFields(flds, data)) return false;
      }
    
      else {
        if (!parseNonDelimitedMsg(body, data)) return false;
      }
    }
    
    // Just about done, see if we need to clean anything up
    if (data.strCity.toUpperCase().startsWith("TOWN OF ")) {
      data.strCity = data.strCity.substring(8).trim();
    }
    
    // Interstate address can not have apt numbers.  If we found one
    // treat it as a mile marker
    if (data.strApt.length() > 0 && I_ADDR_PTN.matcher(data.strAddress).matches()) {
      data.strAddress = data.strAddress + " " + data.strApt;
      data.strApt = "";
    }
    return true;
  }
  
  private static final Pattern MASTER1 = Pattern.compile("([-_/ A-Z0-9]+?)  (?:Box (\\d+) )?(.*) (\\d{4}-\\d{8})");
  private boolean parseNonDelimitedMsg(String body, Data data) {
    Matcher match = MASTER1.matcher(body);
    if (!match.matches()) return false;
    setFieldList("UNIT BOX ADDR APT PLACE CITY CALL ID");
    data.strUnit = match.group(1).trim();
    data.strBox = getOptGroup(match.group(2));
    String addrCall = match.group(3);
    data.strCallId = match.group(4);
    
    // Use smart address parser on what is left.  If we find a city, everything is pretty much done
    Result res = parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK | FLAG_PAD_FIELD, addrCall);
    if (res.getCity().length() > 0) {
      res.getData(data);
      data.strPlace = res.getPadField();
      data.strCall = res.getLeft();
      return (data.strCall.length() > 0);
    }
    
    // No luck there
    // see if we can identify a recognized call description and work from there
    String call = CALL_LIST.getCode(addrCall, true);
    if (call != null) {
      data.strCall = call;
      addrCall = addrCall.substring(0,addrCall.length()-call.length()).trim();
      parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK, addrCall, data);
      data.strPlace = getLeft();
      return true;
    }
    
    // Still no luck, just do the best we can
    parseAddress(StartType.START_ADDR, addrCall, data);
    data.strCall = getLeft();
    return data.strCall.length() > 0;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID1")) return new IdField("Call#: *-(\\d+)", true);
    if (name.equals("ID2")) return new IdField("\\d{4}-\\d{8}");
    if (name.equals("BOX1")) return new MyBox1Field();
    if (name.equals("BOX2")) return new BoxField("Box +(.*)", true);
    return super.getField(name);
  }
  
  private class MyBox1Field extends BoxField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      if (!field.startsWith("Box ")) return false;
      field = field.substring(4).trim();
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(
      "911-Open Line",
      "Abduction/Kidnapping",
      "ACO-General Other",
      "Aircraft-Crash",
      "ATL",
      "BOLO",
      "DIP",
      "Disorder/Domestic-Physical",
      "Disorder/Domestic-Verbal",
      "EMS-Abdominal Pain",
      "EMS-Allergic Reaction",
      "EMS-Altered Mental Status",
      "EMS-Animal Bite",
      "EMS-Assault",
      "EMS-Back Pain",
      "EMS-Bleeding No Trauma",
      "EMS-Breathing Difficulty",
      "EMS-Burn",
      "EMS-Cardiac Arrest",
      "EMS-Chest Pain/Cardiac Problem",
      "EMS-Choking",
      "EMS-Diabetic Problem",
      "EMS-Extremity Injury",
      "EMS-Fall",
      "EMS-Gyn/Miscarriage",
      "EMS-Headache",
      "EMS-Head Injury",
      "EMS-Industrial/Farming Accident",
      "EMS-Medical Alarm",
      "EMS-Overdose",
      "EMS-Poisoning",
      "EMS-Pregnancy/Childbirth",
      "EMS-Seizure",
      "EMS-Sick/Other",
      "EMS-Stroke",
      "EMS-Trauma with Injury",
      "EMS-Uncon/Unresp/Fainting",
      "EMS-Unknown Medical Problem",
      "Escort",
      "Explosion",
      "F-Alarm-Commercial",
      "F-Alarm-Residential",
      "F-Boat-Exposure",
      "F-Brush-Exposure",
      "F-Brush-NoExposure",
      "F-Chimney",
      "F-Gas-Residential",
      "F-HazMat-Small",
      "F-Investigation",
      "F-Mutual Aid Fire",
      "F-Public Service",
      "F-Smoke-Commercial",
      "F-Smoke-Outside",
      "F-Smoke-Residential",
      "F-Structure-Commercial",
      "F-Structure-NonResidential",
      "F-Structure-Residential",
      "F-Vehicle-Exposure",
      "F-Vehicle-NoExposure",
      "Gun Complaint/Shots Fired",
      "Lake-Other",
      "Lockout-Residential",
      "Lockout-Vehicle",
      "Mental-ECO/TDO",
      "Mental/Suicidal",
      "Mental/Suicidal Subject",
      "MVC-Entrapment",
      "MVC-Injury",
      "MVC-No Injuries",
      "MVC-Unk Injury",
      "<New Call>",
      "Rescue-Elevator",
      "Rescue-Water",
      "Suspicious Activity",
      "Traffic-General",
      "Traffic-Road Hazard",
      "TS",
      "Wanted Person",
      "Welfare Check"
  );
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CTOR", ""
  });
  
  private static final String[] CITY_LIST = new String[]{
      // Towns
      "LOUISA",
      "MINERAL",
      "TOWN OF LOUISA",
      "TOWN OF MINERAL",

      // Other Communities
      "APPLE GROVE",
      "BUMPASS",
      "CUCKOO",
      "GUM SPRING",
      "HOLLY GROVE",
      "ORCHID",
      "TREVILIANS",
      "ZION CROSSROADS",
      
      // FLuvanna County
      "TROY",
      
      // Hanover County
      "MONTPELIER",
      
      // Orange County
      "GORDONSVILLE",
      
      // Goochland County
      "MAIDENS"
  };
}
