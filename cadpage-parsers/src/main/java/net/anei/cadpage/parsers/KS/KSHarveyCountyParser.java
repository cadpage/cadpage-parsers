package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Harvey County, KS
 */
public class KSHarveyCountyParser extends FieldProgramParser {

  public KSHarveyCountyParser() {
    super("HARVEY COUNTY", "KS",
          "ADDR/SC! Call_Number:ID! Description:INFO!");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "cad@harveycoumnty.co";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.contains("_DispatchHVCO")) return false;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d+");
    return super.getField(name);
  }
  
  private static final Pattern ADDR_UNIT_PTN = Pattern.compile("(?:(?:[A-Z]+(?:FD|EMS)|[A-Z]+\\d+|1?\\d{3}|H|K|MN|MP|RN) +)+");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_UNIT_PTN.matcher(field);
      if (!match.lookingAt()) abort();
      data.strUnit = match.group().trim();
      field = field.substring(match.end());
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT " + super.getFieldNames();
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BRANDY LAKE",
    "EAST LAKE",
    "ESSEX HEIGHTS",
    "GOLDEN PRAIRIE",
    "HARVEST HILL",
    "LAKE VISTA",
    "MAPLE RIDGE",
    "MEDICAL CENTER",
    "PRAIRIE LAKE",
    "RIVER PARK",
    "ROLLING HILLS",
    "SAND HILL",
    "SPRING LAKE",
    "TRAIL WEST",
    "WHEAT STATE",
    "WILLOW LAKE"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ALARM FIRE",
      "ALARM MEDICAL",
      "ALLERGIC REACTION",
      "ANIMAL BITE (EMS CALL)",
      "BACK PAIN/INJURY",
      "BLEEDING",
      "BREATHING PROBLEM",
      "BURNS",
      "CHASE VEHICLE",
      "CHEST PAIN",
      "CODE BLACK",
      "CODE BLUE",
      "CONTROLLED BURN",
      "CUT/LACERATION",
      "DIABETIC PROBLEMS",
      "DISTURBANCE",
      "FALL",
      "FIGHT",
      "FIRE GRASS",
      "FIRE OTHER",
      "FIRE STRUCTURE",
      "FIRE VEHICLE",
      "GAS SMELL",
      "HEART PROBLEM",
      "INJURY ACCIDENT",
      "LINE/POWER POLE DOWN",
      "MUTAL AID REQUEST",
      "NON-INJURY ACCIDENT",
      "NON-INJURY ACCIDENT MM36",
      "OVERDOSE",
      "PREGNANCY/OB/CHILDBIRTH",
      "PSYCHIATRIC EMERGENCY",
      "SECURITY ALERT",
      "SEIZURE/CONVULSIONS",
      "SHOOTING",
      "SICK (USE SPECIFIC COMPLAINT ON RADIO!!)",
      "SIGN REPAIR/SHOP CALL",
      "SMOKE SMELL",
      "STAND BY (EMS)",
      "STROKE/CVA",
      "TRAFFIC HAZARD",
      "TRANSFER",
      "TRAUMA",
      "UNCONSCIOUS SUBJECT",
      "WEATHER WATCH/WARNING",
      "WELFARE CHECK"
  ); 
}
