package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

public class COArapahoeCountyParser extends FieldProgramParser {
  
  public COArapahoeCountyParser() {
    super("ARAPAHOE COUNTY", "CO",
           "HEAD? ID ( MAP ( GPS1 | ADDR/Z GPS1 ) | ADDR/Z? MAP GPS1 ) GPS2 DUP_ADDR? APT BLDG PLACE CALL UNIT! INFO+");
    setupParseAddressFlags(FLAG_ALLOW_DUAL_DIRECTIONS);
    setupSpecialStreets("BROADWAY", "BROADWAY CIR");
  }
  
  @Override
  public String getFilter() {
    return "metcomdispatch@metcom911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:(?:Comment: (.*) )?Info Only: )?(?:([A-Z]{1,2}-\\d{2}-[A-Z](?:-[A-Z])?) )?(.*?) *([A-Z][,A-Z0-9]+)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // We have two different page formats
    // Check for the pipe delimited field format
    String[] flds = body.split("\\|");
    if (flds.length >= 10) return parseFields(flds, data);
    
    // No go. Check for the undelimited field format
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    setFieldList("INFO MAP ADDR APT PLACE CALL UNIT");
    data.strSupp = getOptGroup(match.group(1));
    data.strMap = getOptGroup(match.group(2));
    body = match.group(3).trim();
    data.strUnit = match.group(4);
    
    String call = CALL_LIST.getCode(body, true);
    if (call != null) body = body.substring(0,body.length()-call.length()).trim();
    parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS, body, data);
    if (call != null) {
      data.strCall = call;
      data.strPlace = getLeft();
    } else {
      data.strCall = getLeft();
      if (data.strCall.length() == 0) return false;
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("HEAD")) return new SkipField("Incident Location Changed to:?|Response Comments:");
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{7}", true);
    if (name.equals("MAP")) return new MapField("[A-Z]{1,2}-\\d{2}-[A-Z](?:-[A-Z])?", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("DUP_ADDR")) return new MyDupAddrField();
    if (name.equals("BLDG")) return new MyBuildingField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyGPSField extends GPSField {
    
    public MyGPSField(int type) {
      super(type);
    }
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!NUMERIC.matcher(field).matches()) return false;
      int pt = field.length() - 6;
      if (pt < 2) return false;
      field = field.substring(0,pt) + '.' + field.substring(pt);
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyDupAddrField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strAddress.length() > 0) {
        if (field.equals(data.strAddress)) return true;
        if (field.equals(getRelativeField(-2))) return true;
      }
      return false;
    }
  }
  
  private class MyBuildingField extends AptField {
    @Override
    public void parse(String field, Data data) {
      data.strApt = append(field, "-", data.strApt);
    }
  }
  
  private static final Pattern INFO_CASE_PTN = Pattern.compile(".*\\bAutomatic Case Number\\(s\\) ?issued for ([ \\w]+?): ([-A-Z0-9]+).");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String delim = "\n";
      for (String part : field.split(",")) {
        part = part.trim();
        if (part.length() == 0) continue;
        Matcher match = INFO_CASE_PTN.matcher(part);
        if (match.matches()) {
          String source = match.group(1).trim();
          if (!data.strSource.contains(source)) {
            data.strSource = append(data.strSource, ",", source);
          }
          String id = match.group(2);
          if (!data.strCallId.contains(id)) {
            data.strCallId = append(data.strCallId, "/", id);
          }
          continue;
        }
        data.strSupp = append(data.strSupp, delim, part);
        delim = ", ";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO SRC ID";
    }
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(
      "Abdominal Pain/Problem",
      "Air Alert 2 Inflight Emergency",
      "Alarm-CO No Sick Parties",
      "Alarm-Fire Alarm Commercial",
      "Alarm-Fire Alarm Residential",
      "Alarm-Medical Alarm",
      "Alcohol Evaluation",
      "Assault/Sexual Assault",
      "Assist - Lift Assist",
      "Assist - Lock In (Child/Pet)",
      "Assist - Lock Out",
      "Assist - Other Agency Assist",
      "Assist - Public Assist",
      "Back Pain (Non-Traumatic)",
      "Breathing Problems",
      "Cardiac or Respiratory Arrest",
      "Chest Pain (Non-Traumatic)",
      "Convulsions/Seizures",
      "Driveway Eye Problems/ Injuries",
      "Electrical Hazard",
      "Falls",
      "Fire - Appliance Fire",
      "Fire - BBQ Grill Fire",
      "Fire - Brush Fire Large",
      "Fire - Brush Fire Small",
      "Fire - Commercial Carrier Fire",
      "Fire - Outside Fire",
      "Fuel Spill Large",
      "Fuel Spill Small",
      "Gas - Commercial Leak/Odor",
      "Gas - Residential Leak/Odor",
      "HazMat",
      "Heart Problems/ A.I.C.D",
      "Hemorrhage/Lacerations",
      "Invest - Lighting Strike",
      "Invest - Odor Commercial",
      "Invest - Odor Outside",
      "Invest - Smoke Inside",
      "Invest - Smoke Outside",
      "Line Down / Transformer",
      "Medical Assist",
      "MVA Extrication",
      "MVA Highway",
      "MVA Injuries",
      "MVA Motorcycle",
      "MVA Rollover",
      "MVA Unknown Injuries",
      "MVA Vehicle Into Building",
      "Overdose/Poisoning (Ingestion)",
      "Psych/Abn Behavior/Suicide Att",
      "Resc - Confined Space Rescue",
      "SF - Comm Str Fire Reported",
      "SF - Multi-Fam Str Fire Report",
      "SF - Outbuilding Fire",
      "SF - Res Str Fire Reported",
      "SF1C - Commercial Str Fire",
      "Sick Person (Spec. Diagnosis)",
      "Stab/Gunshot/Penetrating Traum",
      "Standby In The Area",
      "Stroke(CVA)",
      "Test Call (Do Not Dispatch )",
      "Traffic Pedestrian Acciden",
      "Traumatic Injuries (Specific)",
      "Unconscious/Fainting (Near)",
      "x1A-Abdominal Pain/Problems",
      "x21D-Hemorrhage/Lacerations",
      "x26A-Sick Person",
      "x9E-Cardiac/Resp Arrest"
  );
}
