package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALColbertCountyBParser extends FieldProgramParser {
  
  public ALColbertCountyBParser() {
    super("COLBERT COUNTY", "AL", 
          "ID TYPE:CALL! LOC:ADDR! CITY:CITY! GPS UNIT:UNIT! Comments:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "colbertcoal@911email.net,e-alerts@colbert911.org,DISPATCH";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISPATCH")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("INCIDENT # *(.*)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("GPS")) return new GPSField("[-+]?\\d{1,3}\\.\\d{6,} *, *[-+]?\\d{1,3}\\.\\d{6,}", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_BOX_PTN = Pattern.compile("(\\d+)[ /]?([MF])\\b *(.*)"); 
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        String place = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
        Matcher match = ADDR_BOX_PTN.matcher(place);
        if (match.matches()) {
          data.strBox = match.group(1) + match.group(2);
          place = match.group(3);
        }
        data.strPlace = place;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " BOX PLACE";
    }
  }
}
