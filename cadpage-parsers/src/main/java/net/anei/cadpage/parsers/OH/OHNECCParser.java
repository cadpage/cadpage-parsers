package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHNECCParser extends FieldProgramParser {
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BLUE",  "BLUE ASH",
      "DFTP",  "DEERFIELD TWP",
      "LV",    "LOVELAND",
      "MASO",  "MASON",
      "SHRN",  "SHARONVILE",
      "SY",    "SYMMES",
      "SYCMTP","SYCAMORE TWP"
  });
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:NECC\\|)?[EF]|");
  private static final Pattern PHONE_PTN = Pattern.compile("<tel:(.*)>");

  public OHNECCParser() {
    super(CITY_CODES, "", "OH",
           "LV:CALL! SKIP PLACE? ADDR! CITY XS:X COM:SKIP INFO+");
  }
  
  @Override
  public String getFilter() {
    return "Dispatcher@safety-center.org,rc.340@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (! SUBJECT_PTN.matcher(subject).matches()) return false;
    
    return parseFields(body.split("\\n"), data);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(pt+1).trim();
      super.parse(field, data);
    }
  }
  
  private class MyAddressField extends AddressField {

    @Override
    public boolean checkParse(String field, Data data) {
      field = removePhone(field, data);
      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      field = removePhone(field, data);
      super.parse(field, data);
    }
    
    private String removePhone(String field, Data data) {
      Matcher match = PHONE_PTN.matcher(field);
      if (match.find()) {
        data.strPhone = match.group(1);
        field = field.substring(0,match.start()) + field.substring(match.end());
      }
      return field;
    }

    @Override
    public String getFieldNames() {
      return "PHONE " + super.getFieldNames();
    }
    
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("(")) field = field.substring(1);
      if (field.endsWith(")")) field = field.substring(0,field.length()-1);
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
}
