package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LACalcasieuParishParser extends FieldProgramParser {
  
  public LACalcasieuParishParser() {
    super("CALCASIEU PARISH", "LA", 
          "CALL CALL2/L? ADDR ( CITY! SKIPH? X+? | PLACE CITY! SKIPH? X+? | ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "e911page@cityoflc.us,@calcasieu911.com,72436050";
  }
  
  private static final Pattern DIR_SLASH_PTN1 = Pattern.compile("\\b([NS])/([EW])\\b");
  private static final Pattern DIR_SLASH_PTN2 = Pattern.compile("/([NSEW])/");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("E911400")) break;
      if (body.startsWith("E911:")) {
        body = body.substring(4).trim();
        break;
      }
      return false;
    } while (false);
    body = body.replace("\n", "");
    body = DIR_SLASH_PTN1.matcher(body).replaceAll("$1$2");
    body = DIR_SLASH_PTN2.matcher(body).replaceAll("/$1 ");
    return super.parseFields(body.split("/",-1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("ARCING|DETECTOR|PI\\(MED\\)|RELEASE", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("SKIPH")) return new SkipField("H");
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field,  " INTERSECTN");
      super.parse(field, data);
    }
  }
  
  private static final Pattern MAP_CITY_PTN = Pattern.compile("([A-Z]+\\d+) (.*)");
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_CITY_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strMap = match.group(1);
      String city = match.group(2).trim();
      data.strCity = stripFieldStart(city, "AROUND ");
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "MAP CITY";
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "DEQUI",          "DEQUINCY",
      "HOU.R.",         "SULPHER",
      "L C",            "LAKE CHARLES",
      "LOWER WARD 3",   "LAKE CHARLES",
      "MCNEESE UNIV",   "LAKE CHARLES",
      "SE WARD 5",      "VINTON",
      "SE WARD 6",      "SULPHER",
      "SE WARD 7",      "SULPHER",
      "WESTERN WD2",    "IOWA"
  }); 
}
