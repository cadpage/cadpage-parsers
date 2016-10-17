package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CODeltaCountyParser extends FieldProgramParser {
  
  public CODeltaCountyParser() {
    super("DELTA COUNTY", "CO",
           "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "tech@dcadems.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), 7, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*)\\b(?:APT|UNIT|RM) +(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String place = "";
      String apt = p.getLastOptional(';');
      if (apt.length() == 0) apt = p.getLastOptional(':');
      if (apt.length() > 0) {
        Matcher match = ADDR_APT_PTN.matcher(apt);
        if (match.find()) {
          place = match.group(1).trim();
          apt = match.group(2);
        } else if (apt.length() > 4) {
          place = apt;
          apt = "";
        }
        if (place.length() == 0) place = p.getLastOptional(';'); 
      }
      super.parse(p.get(), data);
      data.strPlace = append(data.strPlace, " - ", place);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }
}
