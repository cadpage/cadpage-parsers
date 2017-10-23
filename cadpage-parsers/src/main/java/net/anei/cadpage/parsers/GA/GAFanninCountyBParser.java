package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAFanninCountyBParser extends FieldProgramParser {
  
  public GAFanninCountyBParser() {
    super("FANNIN COUNTY", "GA", 
          "ID CALL NAME ADDRCITY! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "fannindispatch@fannincountyga.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD CALL")) return false;
    return parseFields(body.split(" : "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d{7}|[A-Z]{2,3}\\d{2}-\\d+", true);
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*), +([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
