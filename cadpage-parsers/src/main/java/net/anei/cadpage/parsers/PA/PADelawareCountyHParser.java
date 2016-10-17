package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PADelawareCountyHParser extends FieldProgramParser {
  
  public PADelawareCountyHParser() {
    super(PADelawareCountyParser.CITY_CODES, "DELAWARE COUNTY", "PA",
           "DATE! TIME! CODE CALL ADDR/S6  X/Z+? ( UNIT INFO/Z+? SRC | INFO INFO/Z+? SRC | INFO/Z SRC )  SRC2 SRC3! NAME? SKIP CITY SKIP_UNIT EXTRA+");
  }
  
  @Override
  public String getFilter() {
    return "deputy71@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Messenger 911")) return false;
    body = stripFieldStart(body, "*");
    if (!parseFields(body.split("\\*"), data)) return false;
    if (data.strPlace.startsWith(data.strApt)) data.strApt = "";
    return true;
  }
  
   @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("SKIP_UNIT")) return new SkipField(UNIT_PATTERN_S, true);
    if (name.equals("SRC")) return new SkipField("[A-Z]{2}", true);
    if (name.equals("SRC2")) return new SkipField("FIRE", true);
    if (name.equals("SRC3")) return new SkipField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("CITY")) return new CityField("[A-Z]{2}", true);
    if (name.equals("EXTRA")) return new ExtraField();
    return super.getField(name);
  
  }
     
  private static final String UNIT_PATTERN_S = "[-A-Z0-9]{1,7}";
  private static final String UNIT_VALIDATE_PATTERN_S = "(?!RAMP$)" + UNIT_PATTERN_S+"(?:/"+UNIT_PATTERN_S+")*/?";
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      super(UNIT_VALIDATE_PATTERN_S, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      field = field.replace("/", ",");
      super.parse(field,data);
    }
  }
  
  private class MyInfoField extends InfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("~") && !field.contains(",")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace("~","").trim();
      data.strSupp = append(data.strSupp, "*", field);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      int ndx = field.lastIndexOf('-');
      if (ndx > 3) field = field.substring(0, ndx).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern CROSS_PATTERN= Pattern.compile(".* / .*|.* /|/ .*");
  private static final Pattern PHONE_PATTERN = Pattern.compile("1?\\d{10}|\\d{3}-\\d{3}-\\d{4}");
  private class ExtraField extends Field {
    @Override
    public void parse(String field, Data data) {
      
      Matcher m = CROSS_PATTERN.matcher(field);
      if (m.matches()) return;
      
      m = PHONE_PATTERN.matcher(field);
      if (m.matches()) {
        data.strPhone = field;
        return;
      }
      
      // If a cross-street slips through without a '/' kill it.
      if (!data.strCross.contains(field)) {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE PLACE";
    }
  }
}
