package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHSummitCountyAParser extends FieldProgramParser {
  
  public OHSummitCountyAParser() {
    this("SUMMIT COUNTY", "OH");
  }
  
  OHSummitCountyAParser(String defCity, String defState) {
    super(OHSummitCountyParser.CITY_LIST, defCity, defState,
           "HNUM ADDR PLACE NAME? CITY CALL! INFO");
  }
  
  @Override
  public String getAliasCode() {
    return "OHSummitCountyA";
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }
  
  private static final Pattern PREFIX_PTN = Pattern.compile("\\*([- /A-Z]+)\\* +");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("From:") && !subject.equals("Alert Notification")) return false;
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    String prefix = "";
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      prefix = match.group(1).trim();
      body = body.substring(match.end());
    }
    if (!parseFields(body.split(","), 5, data)) return false;
    
    // Special case - For Mutual aid calls the place name and address field both contain the name of the
    // the response city, the response address is in the info field.
    if (data.strAddress.equalsIgnoreCase(data.strPlace)) {
      String city = data.strAddress;
      match = CITY_TRAILER_PTN.matcher(city);
      if (match.find()) city = city.substring(0,match.start());
      if (isCity(city)) {
        data.strAddress = data.strPlace = "";
        if (data.strSupp.length() > 0) {
          Result res = parseAddress(StartType.START_PLACE, FLAG_NO_IMPLIED_APT, data.strSupp);
          if (res.isValid()) {
            res.getData(data);
            data.strCity = city;
            data.strSupp = append(data.strPlace, " ", res.getLeft());
            data.strPlace = "";
          } else {
            Parser p = new Parser(data.strSupp);
            parseAddress(p.get(" - "), data);
            data.strSupp = p.get();
          }
        } 
        
        // No info, try to find address in call description
        else {
          String addr = data.strCall;
          data.strCall = "";
          parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT, addr, data);
          data.strCall = append(data.strCall, " - ", getLeft());
        }
      }
    }
    
    data.strCall = append(prefix, " - ", data.strCall);
    return true;
  }
  private static final Pattern CITY_TRAILER_PTN = Pattern.compile(" +(?:CITY|TOWN|TOWNSHIP|VILLAGE)$", Pattern.CASE_INSENSITIVE);
  
  private class HouseNumberField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (!NUMERIC.matcher(field).matches()) abort();
      if (!field.equals("0")) data.strAddress = field;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }
  
  // A name field only happens if the place name contained a two part name
  // separated by a comma
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      data.strName = append(data.strPlace, ", ", field);
      data.strPlace = "";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      
      // Intersections go in the place field rather than the first 2 address fields
      if (data.strAddress.length() == 0) {
        parseAddress(field, data);
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private static final Pattern CODE_PTN = Pattern.compile("[A-Z]+");
  private class MyCityField extends CityField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String upField = field.toUpperCase();
      for (String city : OHSummitCountyParser.CITY_LIST) {
        if (upField.startsWith(city)) {
          data.strCity = field.substring(0,city.length());
          String unit = field.substring(city.length()).trim();
          if (!CODE_PTN.matcher(unit).matches()) data.strUnit = unit;
          return true;
        }
      }
      return false;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CITY UNIT";
    }
  }

  private static final Pattern INFO_CODE_PTN = Pattern.compile("^S\\dD\\d");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CODE_PTN.matcher(field);
      if (match.find()) {
        data.strMap = match.group();
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "MAP INFO";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("HNUM")) return new HouseNumberField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PAREN_DIR_PTN.matcher(addr).replaceAll(" $2 $1 ");
    addr = CLEVE_MASS_PTN.matcher(addr).replaceAll("CLEVELAND MASSILON");
    return addr.trim().replaceAll("  +", " ");
  }
  private static final Pattern PAREN_DIR_PTN = Pattern.compile("(?<=^| )([^&\\d]*) \\(([NSEW])\\)");
  private static final Pattern CLEVE_MASS_PTN = Pattern.compile("\\bCleve-Mass\\b", Pattern.CASE_INSENSITIVE);
}
