package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MDNorthEastParser extends FieldProgramParser {
  
  public MDNorthEastParser() {
    super(CITY_CODES, "NORTH EAST", "MD",
           "CODE CALL ADDR X/Z+? CITY X/Z? DATE TIME! INFO CH UNIT");
  }
  
  @Override
  public String getFilter() {
    return "nefc911@nefc.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! parseFields(body.split("\n"),6 , data)) return false;
    if (data.strCall.startsWith(data.strCode)) data.strCode = "";
    if (data.strCity.equals("OOC")) {
      data.strCity = data.defCity = data.defState = "";
    }
    return true;
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      super.parse(new Parser(field).get('('), data);
    }
  }
  
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional('@');
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("\\b\\d{4}[A-Z]?$");
  private class MyChannelField extends ChannelField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (match.find()) {
        data.strMap = match.group();
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CH MAP";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CH")) return new MyChannelField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "NE",   "North East",
      "ELK",  "Elkton",
      "CHAR", "Charlestown",
      "PV",   "Perryville",
      "RS",   "Rising Sun",
      "PD ",  "Port Deposit",
      "WAR",  "Warwick",
      "CHES", "Chesapeake City",
      "CONO", "Conowingo",
      "EARL", "Earlville",
      "CCLT", "Cecilton",
      "COLO ","Colora",
      
      "OOC", "OOC"
      
  });
}
