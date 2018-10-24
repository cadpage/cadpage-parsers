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
          "( SELECT/2 ADDRCITY1 CALL X PLACE SIMPLE CH UNIT! INFO/N+? ( ID2 INFO/N+? GPS1 GPS2 | GPS1 GPS2 ) " +
          "| ( ADDRCITY1 CALL DUP_CALL? " +
            "| CALL DUP_CALL? ADDRCITY2? " + 
            ") " + 
            "( UNIT! " +
            "| DATEMARK " +
            "| X? PLACE? SIMPLE? CH/L+? ( UNIT! | DATEMARK ) " +
            ") INFO2/N+? ( ID1 INFO/N+? GPS1 GPS2 | GPS1 GPS2 ) END " +
          ")");
  }
  
  @Override
  public String getFilter() {
    return "@sjc911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private boolean getInfo = false;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf("\nIMPORTANT NOTICE!");
    if (pt >= 0) body = body.substring(0, pt).trim();
    getInfo = false;
    
    if (subject.startsWith("Automatic R&R Notification: ")) {
      setSelectValue("1");
      if (!super.parseHtmlMsg(subject, body, data)) return false;
    }
    else if (subject.equals("!")) {
      setSelectValue("2");
      if (!parseFields(body.split("\n"), data)) return false;
    }
    else return false;
    if (data.strUnit.length() == 0) data.msgType = MsgType.RUN_REPORT;
    return true;
  }
  
  private static final String GPS_PTN = "-?\\d{2}\\.\\d{6,}|-361";
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DUP_CALL")) return new MyDupCallField();
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("SIMPLE")) return new SkipField("Simple|Complex|Low|Medium", true);
    if (name.equals("UNIT")) return new UnitField("|(?:(?:[A-Z]+\\d+[A-Z]?|[A-Z]+(?:FD|FDEN|FDCH|FDGR|FDRE|FDTA|FDTR|FOAM)|(?:NLF|OSOLO|POR)[A-Z]+|BTFDTA|BUTWPTA|CFDMULE|CLETWPCH|CLETWPTR|EDWEN|EDWTA|NLFDPOLK|SBFINVST|SBPConv|SMCAS|STARKEAMB|X+|mobile\\d+|Mutual Aid)\\b[, ]*)+", true);
    if (name.equals("DATEMARK")) return new SkipField("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}", true);
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN, true);
    if (name.equals("ID1")) return new IdField("\\[.*\\]", true);
    if (name.equals("ID2")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
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
  
  private static final Pattern VALID_ADDRESS_PTN = Pattern.compile("(?:<UNKNOWN>|\\d|LAT:).*|.*(?:,| at |\\bMM\\d+\\b).*");
  
  private class MyAddressCity1Field extends MyAddressCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (PROQA_PTN.matcher(field).matches()) return false;
      if (!PROQA_PTN.matcher(getRelativeField(+1)).matches() &&
          !VALID_ADDRESS_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }
  
  private class MyAddressCity2Field extends MyAddressCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_PLACE_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
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
      if (field.length() == 0) return true;
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
  
  private class MyInfo2Field extends InfoField {
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
