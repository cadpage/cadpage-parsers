package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CASanLuisObispoCountyBParser extends FieldProgramParser {

  public CASanLuisObispoCountyBParser() {
    super("SAN LUIS OBISPO COUNTY", "CA", 
          "Incident:ID! EMPTY CALL? CALL/SDS ADDR/Z CITY/Z! Map_Page:MAP! EMPTY UNIT!");
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("String Match - ([A-Z]+)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public  String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String extra = null;
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) {
          extra = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
      if (extra != null) {
        for (String part : extra.split(";")) {
          part = part.trim();
          if (part.startsWith("GRID ")) {
            data.strMap = append(data.strMap, "/", part.substring(5).trim());
          } else {
            data.strPlace = append(data.strPlace, " - ", part);
          }
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE MAP";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("SLO County")) return;
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("(null)")) return;
      data.strMap = append(data.strMap, "/", field);
    }
  }
}
