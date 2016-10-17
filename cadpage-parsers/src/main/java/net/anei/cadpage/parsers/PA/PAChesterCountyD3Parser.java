package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyD3Parser extends PAChesterCountyBaseParser {
  
  private static final Pattern DELIM = Pattern.compile(" {2,}");
  
  public PAChesterCountyD3Parser() {
    super("DIGITS DATE TIME CODE CALL ADDRCITY! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "gfc@goshenfireco.org,GFC54@goshenfireco.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // subject is truncated version of address that we don't care about
    // but it has to be non-empty
    if (subject.length() == 0) return false;
    
    // And all of the should treat line breaks as spaces
    body = body.replace('\n', ' ');

    // Split and parse by double blank delimiters
    return parseFields(DELIM.split(body), data);
  }
  
  // Call field strips trailing asterisk marker
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" *")) field = field.substring(0,field.length()-2);
      super.parse(field, data);
    }
  }
  
  // Info field checks for city codes
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("-")) field = field.substring(0,field.length()-1);
      if (field.length() == 0) return;
      String city = CITY_CODES.getProperty(field);
      if (city != null) {
        data.strCity = city;
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DIGITS")) return new SkipField("\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
} 
