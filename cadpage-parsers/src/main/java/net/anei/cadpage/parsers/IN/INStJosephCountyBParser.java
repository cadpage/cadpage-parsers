package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class INStJosephCountyBParser extends HtmlProgramParser {
  
  public INStJosephCountyBParser() {
    super("ST JOSEPH COUNTY", "IN",
          "CALL DUP_CALL? ADDRCITY? ( UNIT! " +
                                  " | DATEMARK " +
                                  " | X? PLACE? SIMPLE? CH/L+? ( UNIT! | DATEMARK ) ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@sjc911.com";
  }
  
  private boolean getInfo = false;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("Automatic R&R Notification: ")) return false;
    
    int pt = body.indexOf("\nIMPORTANT NOTICE!");
    if (pt >= 0) body = body.substring(0, pt).trim();

    getInfo = false;
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strUnit.length() == 0) data.msgType = MsgType.RUN_REPORT;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DUP_CALL")) return new MyDupCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("SIMPLE")) return new SkipField("Simple|Complex", true);
    if (name.equals("UNIT")) return new UnitField("(?:(?:[A-Z]+\\d+[A-Z]?|[A-Z]+(?:FD|FDEN|FDCH|FDGR|FDRE|FDTA|FDTR|FOAM)|(?:NLF|OSOLO|POR)[A-Z]+|BTFDTA|BUTWPTA|CFDMULE|CLETWPCH|CLETWPTR|EDWEN|EDWTA|NLFDPOLK|SBFINVST|SBPConv|SMCAS|STARKEAMB|X+|mobile\\d+|Mutual Aid)\\b[, ]*)+", true);
    if (name.equals("DATEMARK")) return new SkipField("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripProQA(field);
      super.parse(field, data);
    }
  }
  
  private class MyDupCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals(getRelativeField(-1))) return true;
      if (field.length() == 1) return true;
      int fldLen = field.length();
      field = stripProQA(field);
      if (field.length() == fldLen && CALL_LIST.getCode(field) == null) return false;
      data.strCall = append(data.strCall, " - ", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_PLACE_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      String tmp = stripFieldStart(field, "<UNKNOWN>");
      if (tmp.length() > 0) field = tmp;
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return true;
      if (!field.contains("/")) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern NOT_PLACE_PTN = Pattern.compile("Simple|Complex|[A-Z]+\\d+_\\d+");
  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_PLACE_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (getInfo) {
        data.strSupp = append(data.strSupp, "\n", field);
        getInfo = false;
      } else if (field.equals("-")) getInfo = true;
    }
  }
  
  private static final Pattern PROQA_PTN = Pattern.compile("(.*?) *\\(PROQA\\)", Pattern.CASE_INSENSITIVE);
  private static String stripProQA(String field) {
    Matcher match = PROQA_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    return field;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldStart(addr, "<UNKNOWN>");
    return super.adjustMapAddress(addr);
  }
  
  private final static CodeSet CALL_LIST = new CodeSet(
      "Abdominal Pain/Problems",
      "Accident",
      "Allergies/Envenomations",
      "Animal Bites/Attacks",
      "Assault/Sexual Assault",
      "Assist",
      "Back Pain",
      "Breathing Problems",
      "Cardiac Arrest",
      "Chest Pain",
      "Childbirth",
      "Choose Call Type",
      "Citizen Assist",
      "CO Investigation",
      "Commercial Fire",
      "Diabetic",
      "Electrical",
      "Falls",
      "Fire Assist",
      "Gas Leak",
      "Headache",
      "Heart",
      "Heat Exposure",
      "Hemorrhage/Laceration",
      "Investigation",
      "Man Down",
      "Medical",
      "Mutual Aid",
      ">New Call<",
      "Outside Fire",
      "Overdose",
      "Penetrating Trauma",
      "Psych/Suicidal/Attempt",
      "Residential Fire Alarm",
      "Seizure",
      "Sick Person",
      "Smoke",
      "Stabbing",
      "Stroke",
      "Structure Fire",
      "Transfer/Interfacility",
      "Traumatic Injuries",
      "Unconscious/Fainting",
      "Unknown Medical",
      "Vehicle Fire"
  ); 
}
