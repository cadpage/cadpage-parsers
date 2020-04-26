package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Prince Georges County, MD (variant E)
 */
public class MDPrinceGeorgesCountyGParser extends MDPrinceGeorgesCountyBaseParser {
  
  public MDPrinceGeorgesCountyGParser() {
    super("CALL ADDR! APT? PLACE/CS+? X! CH? BOX Units:UNIT% UNIT+ Remarks:INFO");
  }
  
  @Override
  public String getFilter() {
    return "@alert.co.pg.md.us,@c-msg.net,14100,12101,@everbridge.net,89361,87844";
  }
  
  private static final Pattern HTML_FILTER_PTN = Pattern.compile("\n {3,}<p> (.*?)</p>\n {3,}", Pattern.DOTALL);
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<!doctype html>\n")) {
      Matcher match = HTML_FILTER_PTN.matcher(body);
      if (!match.find()) return false;
      body = match.group(1).trim();  
      body = body.replace("\n<br>", " ").replace("\n", " ").replace("<br>", " ");
    }

    return super.parseHtmlMsg(subject, body, data);
  }
  
  private static final Pattern ID_PTN = Pattern.compile("(PF\\d{14}): *");

  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = ID_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end()).trim();
    
    body = body.replace(" Unit:", " Units:");
    body = body.replace(". Remarks:", ", Remarks:");
    if (!parseFields(body.split(","), data)) return false;
    
    fixCity(data);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram().replace("BOX", "BOX CITY ST");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("APT")) return new AptField("# *(.*)|([A-Z]?\\d{1,5}[A-Z]?)", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CH")) return new ChannelField("(?:T?G?|FX)[A-F]\\d{1,2}", true);
    if (name.equals("BOX")) return new BoxField("\\d{4}[A-Z]{0,3}|MA[A-Z]{2}");
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      String call = CALL_CODES.getProperty(field);
      if (call != null) {
        data.strCode = field;
        data.strCall = call;
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  // Cross field only exist if it has the correct keyword
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("btwn")) return true;
      if (!field.startsWith("btwn ")) return false;
      field = field.substring(5).trim();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      field = field.replace(" and ", " / ");
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  // Info field drops ProQA comments
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("ProQA recommends dispatch")) return;
      field = stripFieldStart(field, "CC TEXT:");
      super.parse(field, data);
    }
  }
  
  // Unit fields join together with comma separators
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("WI")) {
        if (!data.strCall.contains("(Working)")) data.strCall += " (Working)";
      } else {
        data.strUnit = append(data.strUnit, ",", field);
      }
    }
  }
  
  static void fixCity(Data data) {
    if (data.strCity.length() > 0) return;
    if (data.strBox.startsWith("MA")) {
      String city = MA_CITY_TABLE.getProperty(data.strBox);
      if (city != null) {
        int pt = city.lastIndexOf('/');
        if (pt >= 0) {
          data.strState = city.substring(pt+1);
          city = city.substring(0,pt);
        }
        data.strCity = city;
      }
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "ACTIVEA1",      "UNCONFIRMED ACTIVE ASSAILANT",
      "ACTIVEA2",      "CONFIRMED ACTIVE ASSAILANT",
      "ALS+",          "Medical Local Expanded",
      "ALS0",          "Medical Local",
      "ALS1",          "Medical Local",
      "ALS2",          "Medical Local Expanded",
      "BARI",          "BARRICADED SUBJECT",
      "BLS+",          "BLS W/Assistance",
      "BLS0",          "BLS PROCEED",
      "BLS1",          "BLS RESPOND",
      "BOMB0",         "BOMB- UNVERIFIED PACKAGE OR HAZARD",
      "BOMB1",         "BOMB/HAZARDOUS DEVICE INVESTIGATION",
      "BOMB2",         "SUSPECTED BOMB/HAZARDOUS DEVICE",
      "BOMB3",         "BOMB/HAZARDOUS DEVICE",
      "COLAP1",        "TREE ON A HOUSE INVESTIGATION",
      "COLAP2",        "VEHICLE INTO STRUCTURE W/O INJURIES",
      "COLAP3",        "VEHICLE INTO STRUCTURE W/ INJURIES",
      "COLAP4",        "STRUCTURE ENTRAPMENT OVER WATER WITH INJURIES",
      "COLAPO",        "TRENCH NOTIFICATION",
      "CONFSP4",       "TRENCH/CONFINED SPACE/BUILDING COLLAPSE W/ ENTRAPMENT",
      "EXPLOD5",       "EXPLOSION",
      "GASLK1",        "GAS LEAK OUTSIDE",
      "GASLK2",        "GAS LEAK OUTSIDE W/SICK PEOPLE",
      "GASLK3",        "STREET ALARM- GAS LEAK INSIDE OR OUTSIDE",
      "GASLK4",        "STREET ALARM- GAS LEAK INSIDE W/SICK PEOPLE ",
      "HARES4",        "HIGH ANGLE RESCUE",
      "HAZBOX",        "HAZARDOUS MATERIALS- BOX ALARM",
      "HAZINV",        "NON-EMERGENT HAZARDOUS MATERIALS INVESTIGATION",
      "HAZLOC",        "HAZARDOUS MATERIALS- STREET ALARM",
      "HAZSER",        "NON-EMERGENT HAZARDOUS MATERIALS SERVICE CALL",
      "INVEST1",       "FIRE ALARM SYSTEM INVESTIGATION",
      "INVEST2",       "HIGH RISK INVESTIGATION",
      "INVEST3",       "VEHICLE FIRE/VEHICLE INCIDENT",
      "INVEST4",       "CO DETECTOR W/SICK PEOPLE",
      "INVEST5",       "LOCK OUT W/FOOD ON THE STOVE",
      "METRO",         "RESCUE LOCAL- TRAIN DERAILMENT AND OR FIRE",
      "METRO/TRAINS",  "TRAIN OR TRACK BED INCIDENT",
      "OUTSID1",       "OUTSIDE/BRUSH FIRE NON-RURAL",
      "OUTSID3",       "BRUSH FIRE/RURAL",
      "PALNE1",        "AIRCRAFT DOWN INVESTIGATION",
      "PLANE0",        "LOW FLYING PLANE INCIDENT",
      "PLANE2",        "SMALL AIRCRAFT DOWN",
      "PLANE3",        "AIRCRAFT DOWN INVOLVING WATER",
      "PLANE4",        "LARGE AIRCRAFT DOWN",
      "RESCUE1",       "RESCUE LOCAL",
      "RESCUE2",       "RESCUE LOCAL WITH ENTRAPMENT",
      "RESCUE3",       "LIMITED ACCESS HIGHWAY INCIDENT",
      "RESCUE4",       "LIMITED ACCESS HIGHWAY INCIDENT W/ENTRAPMENT",
      "RESCUE5",       "WOODROW WILSON BRIDGE INCIDENT",
      "RESCUE6",       "WOODROW WILSON BRIDGE INCIDENT W/ENTRAPMENT",
      "RESCUE7",       "RESCUE LOCAL W/ SERIOUS INJURIES",
      "SERV1",         "NON-EMERGENT SERVICE CALL",
      "SERV2",         "NON-EMERGENT LOCK OUT",
      "STRUCF1",       "APPLIANCE/FIRE OUT",
      "STRUCF2",       "STREET ALARM- FIRE",
      "STRUCF3",       "STREET ALARM- FIRE W/INJURIES",
      "STRUCF4",       "BOX ALARM- HOUSE FIRE",
      "STRUCF5",       "BOX ALARM- HOUSE FIRE W/PEOPLE TRAPPED",
      "STRUCF6",       "BOX ALARM- HIGH RISE FIRE",
      "STRUCF7",       "BOX ALARM- HIGH RISE FIRE W/PEOPLE TRAPPED",
      "TRAIN",         "TRAIN DERAILMENT",
      "WATER0",        "NON-EMERGENT WATER INCIDENT",
      "WATER1",        "VEHICLE IN THE WATER INVESTIGATION",
      "WATER2",        "ANIMAL IN THE WATER INCIDENT",
      "WATER3",        "POOL INCIDENT-DROWNING",
      "WATER4",        "WATER RESCUE/NON-POOL INCIDENT",
      "WATER5",        "WATER INCIDENT- TECHNICAL INCIDENT",
      "WATER6",        "PEOPLE IN THE WATER INCIDENT",
      "WATER7",        "BOAT FIRE- WATER OR DOCK"
  });
  
  private static final Properties MA_CITY_TABLE = buildCodeTable(new String[]{
      "MAAA", "ANNE ARUNDEL COUNTY",
      "MACA", "CALVERT COUNTY",
      "MAAL", "ALEXANDRIA COUNTY/VA",
      "MACC", "CALVERT COUNTY",
      "MACH", "CHARLES COUNTY",
      "MADC", "DC",
      "MAFA", "FAIRFAX COUNTY/VA",
//      "MAHC", null,    // ambiguous
      "MAHO", "HOWARD COUNTY",
      "MAMO", "MONTGOMERY COUNTY",
      "MAMC", "MONTGOMERY COUNTY",
      "MASM", "ST MARYS COUNTY"

  });
}
