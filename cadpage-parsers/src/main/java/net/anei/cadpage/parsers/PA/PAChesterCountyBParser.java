package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Chester County, PA (variant B)
 */
public class PAChesterCountyBParser extends PAChesterCountyBaseParser {
  
  private boolean good;
  
  public PAChesterCountyBParser() {
    super("EMPTY? CALL ( ADDRCITY2 X2? | ADDRPL! X2? ( CITY | APT? INFO+? CITY! ) ) INFO+? DATETIME DATETIME? NAME? PHONE");
  }
  
  @Override
  public String getFilter() {
    return "afc23@comcast.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    good = subject.startsWith("Messenger 911");
    if (!good && !subject.equals("") && !subject.equals("Update")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    return good;
  }
  
  private class EmptyField extends SkipField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return field.length() == 0;
    }
  }
  
  private class MyDateTimeField extends BaseDateTimeField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (! super.checkParse(field, data)) return false;
      good = true;
      return true;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("EMPTY")) return new EmptyField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
} 
