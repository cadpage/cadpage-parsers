package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCRobesonCountyParser extends DispatchOSSIParser{

  public NCRobesonCountyParser() {
    super("ROBESON COUNTY", "NC",
      "( CANCEL ADDR INFO | CALL PLACE? ADDR X/Z+? ( ID PRI | PRI ) INFO+ )");
  }
    
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Text Message / ");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("PRI")) return new PriorityField("(\\d|[A-Z])", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_PTN = Pattern.compile("\\d+.*[A-Z].*|.*/.*", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {

      Matcher match = ADDR_PTN.matcher(field);
      
      if (match.matches()) {
        parse(field, data);
        return true;
      }
      return false;
    }
    
    @Override
    public void parse(String field, Data data) {
      
      if (field.startsWith("1 ") | field.startsWith("1-")) {
        field = field.substring(2).trim();
        if (field.startsWith("BLK ")) {
          field = field.substring(4).trim();
        }
      }
      super.parse(field, data);
    }
  }

  private static final Pattern CODE_PTN = Pattern.compile("(\\d{1,2}-?[A-Z]-?\\d{1,2})\\b *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
  
    @Override
    public void parse(String field, Data data) {

      Matcher match = CODE_PTN.matcher(field);
      
      if (data.strCode.length() == 0) {
        if (match.matches()) {
          data.strCode = match.group(1);
          field = match.group(2);
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CODE";
    }
  }
}
