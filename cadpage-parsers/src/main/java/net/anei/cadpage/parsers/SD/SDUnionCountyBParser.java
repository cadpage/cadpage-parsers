package net.anei.cadpage.parsers.SD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;


public class SDUnionCountyBParser extends SDUnionCountyBaseParser {
  
  public SDUnionCountyBParser() {
    super("ID DATE TIME CALL+? ADDR/Z CITY! PLACE_X/Z+? SRC UNIT! INFO+");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("J:")) return false;
    return super.parseFields(body.split("/"), 8, data);
  }
  
  private static final Pattern DATE_PTN = Pattern.compile("\\d\\d-\\d\\d-\\d\\d");
  private class MyDateField extends DateField {
    public MyDateField() {
      setPattern(DATE_PTN);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace('-', '/');
      super.parse(field, data);
    }
    
  }
  
  private class PlaceCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      
      if (field.endsWith("&")) field = field.substring(0,field.length()-1).trim();
      
      // The first field, and only the first field, is considered a place name if it
      // is not a legitimate address
      if (data.strPlace.length() == 0 && data.strCross.length() == 0 && !isValidAddress(field)) {
        data.strPlace = field;
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{9}");
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
    if (name.equals("PLACE_X")) return new PlaceCrossField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = INTERSECT_PTN.matcher(address).replaceAll("");
    return address;
  }
  private static final Pattern INTERSECT_PTN = Pattern.compile(" +INTERSECT[A-Z]*$");
}
