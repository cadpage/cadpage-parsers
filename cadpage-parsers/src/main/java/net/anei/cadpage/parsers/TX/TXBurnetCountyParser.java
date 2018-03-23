package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBurnetCountyParser extends FieldProgramParser {
  
  public TXBurnetCountyParser() {
    this("BURNET COUNTY", "TX");
  }
  
  public TXBurnetCountyParser(String defCity, String defState) {
    super(defCity, defState, 
          "CFS:ID! CALLTYPE:CALL! PRIORITY:PRI! PLACE:PLACE! ADDRESS:ADDR! CITY:CITY! STATE:ST! ZIP:ZIP! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! INFO/N+ Name:NAME Phone:PHONE Address:SKIP ALERT:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "alert@burnetsheriff.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CFS: ")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        if (data.strPlace.length() == 0) data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      
      // We will fix this later
      data.strAddress = field;
    }
  }
  
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      String addr = data.strAddress;
      data.strAddress = "";
      addr = stripFieldEnd(addr, field);
      addr = stripFieldEnd(addr, data.strState);
      addr = stripFieldEnd(addr, data.strCity);
      parseAddress(addr, data);
      
      if (data.strCity.length() == 0) data.strCity = field;
    }
  }

}
