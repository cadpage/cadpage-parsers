package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lauderdale County, AL
 */
public class ALLauderdaleCountyAParser extends FieldProgramParser {
  
  public ALLauderdaleCountyAParser() {
    super(CITY_TABLE, "LAUDERDALE COUNTY", "AL",
        "PRI:SRC! AD:ADDR/S TIME:SKIP! EV:CALL! CS1:X CS2:X RE:INFO");
  }
  
  @Override
  public String getFilter() {
    return "911paging@florenceal.org,911paging";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("TIME:", " TIME:");
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.length() == 0) {
      if (data.strCross.length() == 0) return false;
      data.strAddress = data.strCross;
      data.strCross = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  // Address field must parse : @<place name> syntax
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional(": @");
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  // Call field has a funny -: convention
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-:", "-");
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  // INFO field may have cell phone # & GPS location
  private static final Pattern CELL_INFO_PTN = 
      Pattern.compile("^ALT# ([\\d\\-]+) ([+\\-]\\d+\\.\\d+ [+\\-]\\d+\\.\\d+)\\b");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = CELL_INFO_PTN.matcher(field);
      if (match.find()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2), data);
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE GPS INFO";
    }
  }
  
  static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "CNTY", "",
      "ANDE", "ANDERSON",
      "FLOR", "FLORENCE",
      "KILL", "KILLEN",
      "LEX",  "LEXINGTON",
      "LEXI", "LEXINGTON",
      "ROGE", "ROGERSVILLE",
      "STFL", "ST FLORIAN",
      "WATL", "WATERLOO"
  });
}
