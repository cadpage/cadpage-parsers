package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXHuntCountyBParser extends FieldProgramParser {
  
  public TXHuntCountyBParser() {
    super("HUNT COUNTY", "TX", 
          "Incident_Type:CALL! Incident_Location:ADDR! City:CITY! Timestamp:DATETIME! City:CITY2!");
  }
  
  @Override
  public String getFilter() {
    return "josh.cato@gmail.com";
  }
  
  private static final Pattern COLONS_PTN = Pattern.compile(" *:[: ]*");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Commerce Fire & Rescue Alert")) return false;
    body = COLONS_PTN.matcher(body).replaceAll(":");
    body = stripFieldEnd(body, ",");
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("CITY2")) return new MyCity2Field();
    return super.getField(name);
  }
  
  private class MyCity2Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strSupp = append(data.strSupp, ", ", field.substring(pt+1).trim());
        field = field.substring(0,pt).trim();
      }
      if (field.length() > 0) data.strCity = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
}
