package net.anei.cadpage.parsers.NM;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NMDonaAnaCountyAParser extends FieldProgramParser {

  public NMDonaAnaCountyAParser() {
    super("DONA ANA COUNTY", "NM",
        "CALL UNIT ID ADDR");
  }

  @Override
  public String getFilter() {
    return "access@mvrda.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if(!subject.equals("Notification")) return false;
    return parseFields(body.split("\\|"), data);
  }

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      field = p.get('(');
      String apt = p.getLast(')');
      
      String cross = p.get(';');
      data.strPlace = p.get();
    
      if(field.startsWith("@")) {
        data.strPlace = field.substring(1) .trim();
        super.parse(cross, data);

      } else {
        int pt = field.indexOf(',');
        if(pt >= 0) {
          data.strCity = field.substring(pt + 1) .trim();
          field = field.substring(0, pt) .trim();
        }

        super.parse(field, data);
        if(cross.startsWith("/")) cross = cross.substring(1) .trim();
        if(cross.endsWith("/")) cross = cross.substring(0, cross.length() - 1) .trim();
        data.strCross = cross;
        
        if(apt.startsWith("#")) apt = apt.substring(1) .trim();
        data.strApt = append(data.strApt, "-", apt);
        
      }
      
    }
  
    @Override
    public String getFieldNames() {
      
      return "PLACE ADDR APT CITY X";
    }
  }
  @Override
  public Field getField(String  name) { 
    
    if(name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);

  }

}
