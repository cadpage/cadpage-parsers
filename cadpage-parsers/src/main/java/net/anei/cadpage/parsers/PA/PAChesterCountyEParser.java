package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyEParser extends PAChesterCountyBaseParser {
  
  private static final Pattern DELIM = Pattern.compile(" \\*");
  
  public PAChesterCountyEParser() {
    super("DATE! TIME! CALL APT ADDRCITY X/Z+? CITY! PLACE_DASH NAME PHONE UNIT EMPTY? SRC?");
  }
  
  @Override
  public String getFilter() {
    return "wgr22@";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Dispatch")) return false;

    // Split and parse by asterisk delimiters
    return parseFields(DELIM.split(body), data);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("*")) field = field.substring(1).trim();
      if (field.startsWith("X-STRT ")) {
        field = field.substring(7).trim();
        int pt = field.indexOf(" - ");
        if (pt >= 0) {
          data.strSupp = field.substring(pt+3).trim();
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "X INFO";
    }
  }
  
  private class MySourceField extends SourceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("Dispatch")) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }
  
} 
