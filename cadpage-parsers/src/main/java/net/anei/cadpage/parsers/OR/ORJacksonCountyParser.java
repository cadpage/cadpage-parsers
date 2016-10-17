package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class ORJacksonCountyParser extends FieldProgramParser {
  
  private static final Pattern SRC_DATE_TIME_PTN = Pattern.compile(" - From +([A-Z0-9]+) +(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)$");
  
  public ORJacksonCountyParser() {
    super(CITY_CODES, "JACKSON COUNTY", "OR",
          "CODE CALL ( PLACE SKIP AT | ADDR ) CITY! PRI:PRI! Unit:UNIT! UNIT+");
  }
  
  @Override
  public String getFilter() {
    return "Messaging@ecso911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! subject.equals("CAD Page") && !subject.equals("Cell Phone Paging system")) return false;
    if (!body.startsWith("DISPATCH:")) return false;
    body = body.substring(9).trim();
    
    Matcher match = SRC_DATE_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strSource = match.group(1);
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      body = body.substring(0,match.start()).trim();
    }
    
    // Look for truncated source date time
    else {
      int pt = body.indexOf(" - From");
      if (pt >= 0) {
        String src = body.substring(pt+7).trim();
        pt = src.indexOf(' ');
        if (pt >= 0) src = src.substring(0,pt);
        data.strSource = src;
      }
    }
    
    body = body.replace("Units:", "Unit:");
    return parseFields(body.split(","), 6, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " SRC DATE TIME";
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseAddress(p.get('['), data);
      data.strCity = p.get(']');
    }
  }
  
  private class AtField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("at ")) return false;
      super.parse(field.substring(3).trim(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('<');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, ",", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("AT")) return new AtField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AS", "ASHLAND",
      "BF", "BUTTE FALLS",
      "CJ", "CAVE JUNCTION",
      "CL", "CRATER LAKE",
      "CP", "CENTRAL POINT",
      "DL", "DIAMOND LAKE",
      "EP", "EAGLE POINT",
      "GH", "GOLD HILL",
      "GP", "GRANTS PASS",
      "JV", "JACKSONVILLE",
      "KE", "KERBY",
      "KF", "KLAMATH FALLS",
      "ME", "MERLIN",
      "MF", "MEDFORD",
      "OB", "O'BRIEN",
      "PH", "PHOENIX",
      "PR", "PROSPECT",
      "RR", "ROGUE RIVER",
      "SC", "SHADY COVE",
      "SE", "SELMA",
      "TA", "TALENT",
      "TR", "TRAIL",
      "WC", "WHITE CITY",
      "WI", "WILLIAMS",
      "WO", "WOLF CREEK",
      "WV", "WILDERVILLE"
  });
}
