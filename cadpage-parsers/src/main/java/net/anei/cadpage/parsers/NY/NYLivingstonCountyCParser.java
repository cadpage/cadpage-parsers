package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYLivingstonCountyCParser extends FieldProgramParser {
  
  public NYLivingstonCountyCParser() {
    super(CITY_CODES, "LIVINGSTON COUNTY", "NY",
          "Inc:CALL! Loc:ADDR! ( Comm_Name:PLACE! Between:X! Venue:CITY! | Venue:CITY! Between:X! Comm_Name:PLACE! | Between:X! Venue:CITY! Comm_Name:PLACE! ) Nature:INFO! Addtl1:INFO! Addtl2:INFO! Prty:PRI! Caller:NAME! Inc#:ID! Phone:PHONE!");
  }
  
  @Override
  public String getFilter() {
    return "donotrespond@co.livingston.ny.us";
  }
  
  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("\n", "");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt >= 0) field = field.substring(pt+1).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern NAME_TRAIL_COMMA_PTN = Pattern.compile(",+$");
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = NAME_TRAIL_COMMA_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BENNINGT",     "BENNINGTON",
      "CALEDONI",     "CALEDONIA",
      "COVINGTO",     "CONVINGTON",
      "DANSVILL",     "DANSVILLE",
      "GAINESVI",     "GAINESVILLE",
      "GENE FALLS",   "GENESEE FALLS",
      "GROVELAN",     "GROVELAND",
      "LEICESTE",     "LEICESTER",
      "MIDDLEBU",     "MIDDLEBURY",
      "MT MORRI",     "MT MORRIS",
      "N DANSVI",     "N DANSVILLE",
      "ORANGEVI",     "ORANGEVILLE",
      "WETHERSF",     "WETHERSFIELD",
      // SILVER SPRINGS ???
      "SPRINGWA",     "SPRINGWATER"
  });
}
