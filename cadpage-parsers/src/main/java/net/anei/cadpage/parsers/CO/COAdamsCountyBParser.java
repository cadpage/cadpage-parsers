package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class COAdamsCountyBParser extends FieldProgramParser {
  
  public COAdamsCountyBParser() {
    super("ADAMS COUNTY", "CO", 
          "( NEW_ADDR ID MAP ( ID8/L ID9/L OLD_ADDR | OLD_ADDR ID8/L ID9/L ) APT ( EMPTY PLACE! | PLACE! EMPTY ) CALL/SDS UNIT " +
          "| ID? MAP ADDR ID8/L ID9/L APT EMPTY PLACE CALL UNIT? " + 
          ") CASE/L? CMD:CH END");
  }
  
  @Override
  public String getFilter() {
    return "smfrrelay@smfra.com,smtprelay@SMFRA.onmicrosoft.com";
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 150; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  private static final String MAP_PTN_STR = "([A-Z]-\\d{1,2}-[A-Z](?:-[A-Z])?)";
  private static final Pattern MASTER1 = Pattern.compile("([A-Z ]+?) -(.*?) " + MAP_PTN_STR + " (.*)");
  private static final Pattern MASTER2 = Pattern.compile("([A-Z ]+)RESPOND(.*?)" + MAP_PTN_STR + " (.*?) Cmnd Chnl:(.*)");
  private static final Pattern MASTER3 = Pattern.compile(MAP_PTN_STR + " (.*?)(?: ?([A-Z]+\\d+(?:,[A-Z0-9,]*)?))?");
  private static final Pattern ADDR_PLACE_PTN1 = Pattern.compile("(.*?) \\((.*)\\)");
  private static final Pattern PLACE_CALL_PTN1 = Pattern.compile("\\((.*)\\) *(.*)");
  private static final Pattern PLACE_CALL_PTN2 = Pattern.compile("([-&. A-Z]*[A-Z]{3,}(?<!MVA)) *(.*)");
  

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.endsWith(" Info:")) return false;
    
    body = stripFieldStart(body, "Resp.Info:");
    body = stripFieldStart(body, "RI:");
    
    String[] flds = body.split("\\|", -1);
    if (flds.length > 3) {
      return parseFields(flds, data);
    }
    
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("SRC CALL MAP ADDR APT");
      data.strSource = match.group(1).trim();
      data.strCall = match.group(2).trim();
      data.strMap = match.group(3).trim();
      parseAddress(match.group(4), data);
      return true;
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("SRC CALL MAP ADDR APT CH");
      data.strSource = match.group(1).trim();
      data.strCall = match.group(2).trim();
      data.strMap = match.group(3);
      parseAddress(match.group(4).trim(), data);
      data.strChannel = match.group(5).trim();
      return true;
    }
    
    match = MASTER3.matcher(body);
    if (match.matches()) {
      setFieldList("MAP ADDR APT PLACE CALL UNIT");
      data.strMap = match.group(1);
      String addrCall = match.group(2).trim();
      data.strUnit = getOptGroup(match.group(3));
      
      String call = CALL_LIST.getCode(addrCall);
      if (call != null) {
        data.strCall = call;
        String addr = addrCall.substring(0, addrCall.length()-call.length());
        if (addr.endsWith("UPDATE - ")) {
          data.strCall = "UPDATE - " + data.strCall;
          addrCall = addrCall.substring(0, addrCall.length()-9);
        }
        addrCall = addrCall.trim();
        match = ADDR_PLACE_PTN1.matcher(addr);
        if (match.matches()) {
          parseAddress(match.group(1).trim(), data);
          data.strPlace = match.group(2).trim();
        }
        else {
          parseAddress(StartType.START_ADDR, addr, data);
          data.strPlace = getLeft();
        }
        return true;
      } else {
        parseAddress(StartType.START_ADDR, addrCall, data);
        call = getLeft();
        match = PLACE_CALL_PTN1.matcher(call);
        if (match.matches()) {
          data.strPlace = match.group(1).trim();
          call = match.group(2);
        }
        else if ((match = PLACE_CALL_PTN2.matcher(call)).matches()) {
          data.strPlace = match.group(1).trim();
          call = match.group(2);
        }
        data.strCall = call;
        return (data.strCall.length() > 0);
      }
    }
    
    return false;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NEW_ADDR"))  return new MyNewAddressField();
    if (name.equals("OLD_ADDR")) return new MyOldAddressField();
    if (name.equals("MAP")) return new MapField(MAP_PTN_STR, true);
    if (name.equals("ID")) return new IdField("(\\d{4}-\\d{7})", true);
    if (name.equals("ID8")) return new IdField("\\d{8}");
    if (name.equals("ID9")) return new IdField("\\d{9}");
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CASE")) return new IdField("Case# *(.*)", true);
    return super.getField(name);
  }
  
  private static final Pattern NEW_ADDR_PTN = Pattern.compile("(?:Incident Location Changed to|Inc Address Changed to|Inc Address Update): *(.*)");
  private class MyNewAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = NEW_ADDR_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCall = "UPDATE";
      parse(match.group(1), data);
      return true;
    }
    
    @Override
    public String getFieldNames() {
      return "CALL? ADDR APT";
    }
  }
  
  private class MyOldAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strAddress = "";
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Case#")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(       
      "267 Unconscious/Fainting (Near)",
      "279 Assist - Lift Assist",
      "279 Sick Person (Spec. Diagnosis)",
      "282 Assist - Lift Assist",
      "282 Traumatic Injuries (Specific)",
      "305 Hemorrhage/Lacerations",
      "Abdominal Pain/Problem",
      "Abdominal Pain/Problems",
      "Alarm-Fire Alarm Commercial",
      "Alarm-Fire Alarm Residential",
      "Alarm-Fire Alarm High Occupa",
      "Allergies/Envenomation",
      "Assault/Sexual Assault",
      "Assist - Lift Assist",
      "Assist - Lock Out",
      "Assist - Public Assist",
      "Assist - Water Prob/Shut Off",
      "Back Pain (Non-Traumatic)",
      "Breathing Problems",
      "Chest Pain (Non-Traumatic)",
      "Convulsions/Seizures",
      "Falls",
      "Fire - Brush Fire Hwy",
      "Fire - Brush Fire Large",
      "Fire - Brush Fire Small",
      "Fire - Commercial Carrier Fire",
      "Fire - Illegal Burn",
      "Fire - Outside Fire",
      "Fire - Unknown Fire",
      "Fire-Brush Fire Large",
      "Fire-Brush Fire Small",
      "Fire-Illegal Burn",
      "Fire-Vehicle Fire",
      "Gas-Commercial Leak",
      "Heat/Cold Exposure",
      "Hemorrhage/Lacerations",
      "Highway",
      "Injuries",
      "Invest - Hazmat",
      "Invest - Odor Commercial",
      "Invest - Smoke Outside",
      "Invest-Odor Commercial",
      "Line Down / Transformer",
      "Medical Assist",
      "Medical-Highway",
      "MVA Highway",
      "MVA Injuries",
      "MVA Non-injury",
      "MVA Rollover",
      "MVA Traffic Pedestrian Accidnt",
      "MVA Unknown Injuries",
      "Non-injury",
      "Overdose/Poisoning (Ingestion)",
      "Psych/Abn Behavior/Suicide Att",
      "Psych Problems",
      "Resc - Animal Rescue",
      "Rollover",
      "SF - Res Str Fire Reported",
      "SF-Outbuilding Fire",
      "SF-Res Structure Fire Reported",
      "Sick Person (Spec. Diagnosis)",
      "Sick Person (Specific Diag)",
      "Special Event Coverage",
      "Standby In The Area",
      "Standby InThe Area",
      "Stroke(CVA)",
      "Test Call (Do Not Dispatch)",
      "Traumatic Injuries (Specific)",
      "Unconscious/Fainting (Near)",
      "x17B-Falls",
      "x26A-Sick Person",
      "x6C-Breathing Problems",
      "x6D-Breathing Problems",
      "x9E-Cardiac/Resp Arrest"
  );
}
