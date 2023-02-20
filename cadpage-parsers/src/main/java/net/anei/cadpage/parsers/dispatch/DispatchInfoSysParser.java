package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchInfoSysParser extends FieldProgramParser {
  
  protected DispatchInfoSysParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "CODE_CALL ADDR/S6! PLACE? CITY_ST ST_ZIP? INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split(",", -1), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY_ST")) return new MyCityStateField();
    if (name.equals("ST_ZIP")) return new MyStateZipField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(?:(ALL CALL|\\*{3} .*? \\*{3}) +)?([/A-Z0-9]+)-(\\S.*)", Pattern.CASE_INSENSITIVE);
  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
      if (field.isEmpty()) return;
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      String prefix = match.group(1);
      data.strCode = match.group(2);
      data.strCall = match.group(3);
      if (prefix != null) data.strCall = prefix + " " + data.strCall;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern CLEAN_TERM_PTN = Pattern.compile(" (?:DR|RD|ST)\\b|[- ]");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      
      // Intersections go in the place field rather than the first 2 address fields
      if (data.strAddress.length() == 0) {
        parseAddress(field, data);
      } else {
        
        // Intersections are duplicated in the place field.  If this
        // matches the previous address field, ignore it
        String addr = getRelativeField(-1);
        addr = stripFieldStart(addr, "0 ");
        if (cleanup(field).equals(cleanup(addr))) return;
        super.parse(field, data);
      }
    }
    
    private String cleanup(String field) {
      return CLEAN_TERM_PTN.matcher(field).replaceAll("");
    }
  }

  private class MyCityStateField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) {
        if (!field.equals(data.defState) || field.length() == 0) return false;
        data.strState = field;
        return true;
      }
      if (field.endsWith(' ' + data.defState)) {
        data.strState = data.defState;
        data.strCity = field.substring(0, field.length()-3).trim();
        return true;
      } else {
        if (field.length() == 0)  return true;
        return super.checkParse(field, data);
      }
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
  
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("(?:(OH) +)?(\\d{5})(?: +(.*))?");
  private class MyStateZipField extends MyInfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = STATE_ZIP_PTN.matcher(field);
      if (!match.matches()) return false;
      String state = match.group(1);
      if (state != null) data.strState = state;
      if (data.strCity.length() == 0) data.strCity =  match.group(2);
      String info = match.group(3);
      if (info != null) super.parse(info, data);;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "ST CITY " + super.getFieldNames();
    }
  }
  
  private class MyCityField extends MyInfoField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field);
      if (res.getCity().length() == 0) return false;
      res.getData(data);
      super.parse(res.getLeft(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CITY " + super.getFieldNames();
    }
  }

  private static final Pattern INFO_CODE_PTN = Pattern.compile("\\bS\\dD\\d$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CODE_PTN.matcher(field);
      if (match.find()) {
        data.strMap = match.group();
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO MAP";
    }
  }
}
