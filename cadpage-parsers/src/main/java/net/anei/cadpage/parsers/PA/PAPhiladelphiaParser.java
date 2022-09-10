package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAPhiladelphiaParser extends FieldProgramParser {
  
  public PAPhiladelphiaParser() {
    super("PHILADELPHIA", "PA", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITYST! CITY:CITY! PRI:PRI! DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT! INFO:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("10-8 Systems Alert")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(", USA");
      if (pt >= 0) {
        String apt = field.substring(pt+5).trim();
        field = field.substring(0,pt).trim();
        data.strApt = append(data.strApt, "-", stripFieldStart(apt, "#"));
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " APT";
    }
  }
}
