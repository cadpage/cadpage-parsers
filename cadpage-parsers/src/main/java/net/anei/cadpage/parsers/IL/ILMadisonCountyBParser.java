package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILMadisonCountyBParser extends FieldProgramParser {
  
  public ILMadisonCountyBParser() {
    super("MADISON COUNTY", "IL", 
          "CALL ID PLACE ADDR1 ADDR1 ADDR2 APT CITY ST ZIP! EMPTY EMPTY INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "lawmancm@idsapplications.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true); 
    if (name.equals("ADDR1")) return new MyAddressField(false);
    if (name.equals("ADDR2")) return new MyAddressField(true);
    if (name.equals("ZIP")) return new MyZipField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    
    private boolean lastAddress;
    
    public MyAddressField(boolean lastAddress) {
      this.lastAddress = lastAddress;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, " ", field);
      if (lastAddress) {
        field = data.strAddress.replace('@',  '&');
        data.strAddress = "";
        super.parse(field,  data);
      }
    }
  }
  
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0 && (field.length() != 5 || !NUMERIC.matcher(field).matches())) abort();
      if (data.strCity.length() > 0) return;
      super.parse(field,  data);
    }
  }

}
