package net.anei.cadpage.parsers.ID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDTwinFallsCountyCParser extends FieldProgramParser {
  
  public IDTwinFallsCountyCParser() {
    super("TWIN FALLS COUNTY", "ID", 
          "UNIT ID CALL ADDRCITY PLACE INFO! END");
  }
  
  @Override
  public String getFilter() {
    return "cad@sircomm.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" -- -- ", " --  -- ");
    return parseFields(body.split(" -- "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID"))  return new IdField("CFS\\d{6}-\\d{4}", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = ADDR_ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        zip = match.group(2);
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip != null) city = zip;
      data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override 
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^| *; )\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - LOG - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
