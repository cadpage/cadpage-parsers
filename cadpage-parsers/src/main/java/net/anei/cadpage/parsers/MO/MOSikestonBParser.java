package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOSikestonBParser extends FieldProgramParser {
  
  public MOSikestonBParser() {
    super("SIKESTON", "MO", 
          "Call_Number:ID! Situation:CALL! Address:ADDR! Zone:MAP SubZone:MAP/D");
  }
  
  @Override
  public String getFilter() {
    return "CAD_INCIDENT@sikeston.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD INCIDENT")) return false;
    int pt = body.indexOf("\nSent");
    if (pt >= 0) body = body.substring(0,pt);
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PTN = Pattern.compile("([^,]*)(?:, *([ A-Za-z]+))?(?:,(?:(?: *(\\d{5}))? *\\b([A-Z]{2}))?)?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        String city = match.group(2);
        String zip = match.group(3);
        String state =  match.group(4);
        
        if (city == null) city = zip;
        if (city != null) data.strCity = city.trim();
        if (state != null) data.strState = state;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
}
