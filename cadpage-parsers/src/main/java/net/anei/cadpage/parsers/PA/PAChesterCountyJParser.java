/*
Chester County, PA
*/

package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyJParser extends PAChesterCountyBaseParser{

  public PAChesterCountyJParser() {
    super("ID CALL ADDRCITY X PLACE PLACE_PHONE CITY! INFO+? EMPTY TIME DATE!");
  }

  @Override
  public String getFilter() {
    return "afc23@fdcms.info";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private class AddressCityField extends BaseAddressCityField {
    
    @Override
    public void parse(String field, Data data) {
      
      // The base class will not accept fields without a comma.
      // but we have a few that we have to deal with :(
      if (!field.contains(",")) {
        if (field.endsWith("(NV)")) {
          field = field.substring(0,field.length()-4).trim();
          parseAddress(field, data);
          return;
        }
      }
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      if (field.trim().equals("DETAILS TO FOLLOW")) return;
      data.strSupp += field;
    }
    
    @Override
    public boolean doNotTrim() {
      return true;
    }
  }
  
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new AddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIME")) return new TimeField("(\\d\\d:\\d\\d):", true);
    return super.getField(name);
  }
}
