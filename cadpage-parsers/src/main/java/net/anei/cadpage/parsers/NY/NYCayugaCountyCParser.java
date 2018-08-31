package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYCayugaCountyCParser extends FieldProgramParser {
  
  public NYCayugaCountyCParser() {
    super(CITY_CODES, "CAYUGA COUNTY", "NY", 
          "SRC CALL DISP ADDRCITY UNIT! UNIT/C+? INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "2083399494,2083399308";
  }
  
  private static final String MARKER = "Cayuga County E911: ";
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (body.startsWith(MARKER)) {
        body = body.substring(MARKER.length()).trim();
        break;
      }
      
      if (subject.equals("From Cayuga County 911")) break;
      
      return false;
    } while (false);
    int pt = body.indexOf("\nText Stop");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{3,4}", true);
    if (name.equals("DISP")) return new SkipField("DISP", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("BOX")) return new BoxField("[A-Z0-9]+", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]{3,7}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      data.strPlace = p.getLastOptional(';');
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d\\d \\d\\d \\d\\d) (\\d\\d/\\d\\d/\\d{4}) - +(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (match.matches()) {
        if (data.strTime.length() == 0) {
          data.strTime = match.group(1).replace(' ', ':');
          data.strDate = match.group(2);
        }
        field = match.group(3);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " TIME DATE";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CTT",    "CATO",
      "FHA",    "FAIR HAVEN",
      "IRA",    "IRA",
      "STE",    "STERLING",
      "VIC",    "VICTORY",
      "WOL",    "WOLCOTT"
  });

}
