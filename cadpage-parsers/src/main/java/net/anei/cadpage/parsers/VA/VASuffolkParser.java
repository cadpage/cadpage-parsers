package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VASuffolkParser extends DispatchOSSIParser {
  
  public VASuffolkParser() {
    super("SUFFOLK", "VA",
          "( CALL1! ADDR CITY1! " + 
          "| UNIT? CALL ADDR X/Z+? DATETIME ( ID! | PHONE ID! | NAME PHONE/Z ID! | NAME ID | ) ) INFO/N+");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[^ ]*[,0-9][^ ]*", true);
    if (name.equals("CALL1")) return new CallField("\\{[A-Z0-9]+\\}.*", true);
    if (name.equals("CITY1")) return new MyCity1Field();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private class MyCity1Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      data.strCity = convertCodes(field, CITY_CODES);
    }
  }
  
  // The Phone and ID fields look a lot alike.  But there is always an
  // ID field and it comes after the optional phone field.  So we will
  // sort them out by checking the following field
  private static final Pattern PHONE_ID_PTN = Pattern.compile("\\d{10}");
  
  private class MyPhoneField extends PhoneField {
    
    // The phone field has a default pattern that we need to zap
    public MyPhoneField() {
      super(null);
    }
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!PHONE_ID_PTN.matcher(field).matches()) return false;
      if (!PHONE_ID_PTN.matcher(getRelativeField(+1)).matches()) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!PHONE_ID_PTN.matcher(field).matches()) return false;
      if (PHONE_ID_PTN.matcher(getRelativeField(+1)).matches()) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "SUF", "SUFFOLK"
  });
}
