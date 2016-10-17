package net.anei.cadpage.parsers.CA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class CASuisunCityAParser extends DispatchA3Parser {

  public CASuisunCityAParser() {
    super("", "SUISUN CITY", "CA", 
        "( CALL ADDR | CALL_ADDR ) Apt:APT! CITY! Com:INFO1! Units:UNIT? INFO+", FA3_NBH_MAP);
  }
  
  private static final Pattern MISSING_ASTERISK_PTN = Pattern.compile("(?<!\\*)(?= (?:Com|Units):)");
  
  @Override
  public String getFilter() {
    return "cad@suisun.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("iPage")) return false;
    body = MISSING_ASTERISK_PTN.matcher(body).replaceAll("*");
    if (!parseMsg(body, data)) return false;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    if (name.equals("INFO")) return new MyBaseInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("@")) return false;
      super.parse(field.substring(1).trim(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  
  private class MyCallAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt < 0) return false;
      data.strCall = field.substring(0,pt).trim();
      parseAddress(field.substring(pt+1).trim(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames();
    }
  }
  
  public class MyBaseInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {

      // Apparently they put spaces on both sides of cross street // 
      // which is different from everybody else in creation.
      field = field.replace("// ", "//");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS MAP";
    }
  }
}
