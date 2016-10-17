package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class TNBlountCountyAParser extends FieldProgramParser {
  
  public TNBlountCountyAParser() {
    super("BLOUNT COUNTY", "TN",
           "CALL ADDR INFO DATETIME!");
  }
  
  @Override
  public String getFilter() {
    return "page@tnacso.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\\|"), 4, data);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(",", "");
      Parser p = new Parser(field);
      parseAddress(p.get(';'), data);
      data.strCity = p.get('(');
      data.strCross = p.get(')');
      data.strSupp = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X";
    }
  }
  
  private class MyDateTimeField extends DateTimeField {
    public MyDateTimeField() {
      super("\\d{4}-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strDate = field.substring(5,7) + '/' + field.substring(8,10) + '/' + field.substring(0,4);
      data.strTime = field.substring(11);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
}
