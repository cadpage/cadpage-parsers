package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;


public class NYSuffolkCountyFiretrackerParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^\\*\\* ?([A-Z ]+) ?\\*\\* \\[([- A-Z0-9]+)\\] +(?:\\(([- A-Z0-9]+)\\) +)?");
  
  public NYSuffolkCountyFiretrackerParser() {
    this("SUFFOLK COUNTY", "NY");
  }
  public NYSuffolkCountyFiretrackerParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState, 
           "ADDR/S! C/S:X? TOA:TIME? Add't_Info:INFO");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@firetracker.net";
  }
  
  @Override
  public   String getAliasCode() {
    return "NYSuffolkCountyFiretracker";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("FirePage")) return false;
    body = body.replace('\n', ' ');
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strSource = match.group(1).trim();
    data.strCode = match.group(2).trim();
    data.strCall = getOptGroup(match.group(3));
    if (data.strCall.length() == 0) {
      data.strCall = CODE_TABLE.getCodeDescription(data.strCode);
      if (data.strCall == null) data.strCall = data.strCode;
    }
    body = body.substring(match.end()).trim();
    
    int pt = body.lastIndexOf('[');
    if (pt >= 0 && "[FireTracker]".startsWith(body.substring(pt))) {
      body = body.substring(0,pt).trim();
    }
    
    // Rule out NYNassauCountyFiretracker calls
    if (body.startsWith("[")) return false;

    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC CODE CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_PTN = Pattern.compile(" +(\\d\\d?/\\d\\d?/\\d{4})$");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_PTN.matcher(field);
      if (match.find()) {
        data.strDate = match.group(1);
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " DATE";
    }
  }
  
  private static final StandardCodeTable CODE_TABLE = new StandardCodeTable();
  
  private static final String[] CITY_LIST = new String[]{
    "MASTIC BEACH",
    "SHIRLEY",
    "SMITH POINT",
    
    // Nassau County
    "ALBERTSON",
    "EAST WILLISTON",
    "GARDEN CITY PARK",
    "MANHASSET LAKEVILLE",
    "MINEOLA",
    "NEW HYDE PARK",
    "PORT WASHINGTON",
    "ROSYLN",
    "WESTBURY",
    "WILLISTON PARK"
  };
}
