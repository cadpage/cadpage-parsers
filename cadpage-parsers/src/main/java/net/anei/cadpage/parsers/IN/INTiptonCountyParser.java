package net.anei.cadpage.parsers.IN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class INTiptonCountyParser extends DispatchOSSIParser {
  
  public INTiptonCountyParser() {
    super(CITY_CODES, "TIPTON COUNTY", "IN",
          "( GPS1 GPS2 UNIT ADDR/S ( PLACE CALL | CALL ) " +
          "| UNIT_CALL ADDR CITY! " + 
          "| UNIT ( ADDR/SZ CALL " + 
                 "| CALL MAP? ( PLACE ADDR/Z CITY! " +
                             "| ADDR/Z CITY! " +
                             "| PLACE ADDR/S! " +
                             "| ADDR/S! " +
                             ") " +
                 ") " +
          ") INFO/N+");
    setupCities(CITY_LIST);
  }
  
  @Override
  public String getFilter() {
    return "CAD@tipco.com,CAD@tiptoncounty.in.gov";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldEnd(body, "(null)");
    if (!body.startsWith("CAD:")) {
      if (subject.equals("CAD TEXT")) {
        body = "CAD:" + body;
      } else if (subject.contains(";") && !subject.contains(":")) {
        body = "CAD:" + subject + ':' + body;
      }
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field  getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT_CALL")) return new MyUnitCallField();
    if (name.equals("MAP")) return new MapField("\\d+|CITY");
    return super.getField(name);
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!checkCall(field)) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  private static final Pattern UNIT_CALL_PTN = Pattern.compile("\\{([A-Z0-9]+)\\} *(.*)");
  private class MyUnitCallField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = match.group(1);
      data.strCall = match.group(2);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }
  
  
  
  @Override
  public boolean checkCall(String call) {
    return CALL_LIST.contains(call);
  }

  private static Set<String> CALL_LIST = new HashSet<>(Arrays.asList(
      "1050 pi",
      "ABDOMINAL PAIN/PROBLEMS",
      "ALARM-CARBON MONOXIDE",
      "ALARM-INSTITUTIONAL",
      "ALARM-RESIDENTIAL FIRE",
      "ALLERGIC REACTION/STINGS",
      "ASSAULT/SEX ASSAULT",
      "BREATHING PROBLEMS",
      "CARDIAC ARREST/POSSIBLE",
      "CHECK OUT",
      "CHEST PAINS",
      "DIABETIC EMERGENCY",
      "FIRE ALARM COMMERCIAL/BUSINES",
      "Fire is out",
      "FUNCTIONAL DEVICES",
      "HAZMAT-LEVEL 1",
      "HEMORRAGE/LACERATION",
      "INJURED IN FALL",
      "MVA/PERSONAL INJURY",
      "MVA UNKNOWN",
      "OUTSIDE FIRE-FIELD/WOODS",
      "Page oncoming shift",
      "PATIENT ASSIST",
      "Request utilities",
      "RESCUE EXTRICATION/ENTRAPMENT",
      "SEIZURE",
      "SEVERE WEATHER",
      "SICK PERSON",
      "STROKE",
      "STRUCTURE FIRE-GARAGE/BARN",
      "STRUCTURE FIRE RESIDENTIAL",
      "TRANSFER /TRANSPORT",
      "TREE FIRE",
      "UNCLASSIFIED MEDICAL",
      "UNCONSCIOUS PERSON / FAINTING",
      "UNKNOWN MEDICAL PROBLEM",
      "VEHICLE FIRE PASS CAR/TRUCK"
      
  ));
  
  private static final Properties CITY_CODES =  buildCodeTable(new String[]{
      "ATL", "ATLANTA",
      "SHR", "SHARPSVILLE",
      "TIP", "TIPTON",
      "WDF", "WINDFALL CITY"
  });
  
  private static final String[] CITY_LIST = new String[]{
      "ARCADIA"
  };
}
