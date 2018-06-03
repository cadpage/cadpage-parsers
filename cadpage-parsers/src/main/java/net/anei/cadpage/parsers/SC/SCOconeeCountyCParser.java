package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCOconeeCountyCParser extends FieldProgramParser {
  
  public SCOconeeCountyCParser() {
    super("OCONEE COUNTY", "SC", 
        "CFS:ID! CALLTYPE:CALL! PRIORITY:PRI! PLACE:PLACE! ADDRESS:ADDR! CITY:CITY! STATE:ST! ZIP:ZIP! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "senecapolicedispatch@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    String addr = data.strAddress;
    data.strAddress = "";
    addr = stripFieldEnd(addr, data.strState);
    addr = stripFieldEnd(addr, data.strCity);
    parseAddress(addr, data);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = field;
    }
  }
  
  private class MyZipField extends ZipField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        data.strCity = field;
      } else {
        data.strAddress = stripFieldEnd(data.strAddress, field);
      }
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Email sent to ")) return;
      super.parse(field, data);
    }
  }
}
