package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VASouthamptonCountyAParser extends DispatchOSSIParser {
  
  public VASouthamptonCountyAParser() {
    super(VASouthamptonCountyParser.CITY_LIST, "SOUTHAMPTON COUNTY", "VA",
           "( SELECT/1 ADDR/S X? TIME CALL! geo:GPS? | VADDR CALL! ) INFO/N+");
    addRoadSuffixTerms("CRESCENT");
  }
  
  @Override
  public String getFilter() {
    return "@shso.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private String select;

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Two different formats with different field separators
    String[] flds = body.split(";");
    if (flds.length >= 3 ) {
      select = "1";
    } else  {
      select = "2";
      int pt = body.indexOf("\n\n");
      if (pt >= 0) body = body.substring(0,pt).trim();
      flds = body.split("\n");
    }
    return parseFields(flds, data);
  }

  @Override
  protected String getSelectValue() {
    return select;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("VADDR")) return new MyValidAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    return super.getField(name);
  }
  
  // Address field that saves valid status of address
  private static final Pattern VALID_ADDR_PTN = Pattern.compile("\\d{3,} +.*");
  private class MyValidAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int flags = FLAG_CHECK_STATUS | FLAG_ANCHOR_END;
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.equals("VA")) city = p.getLastOptional(',');
      if (city.length() > 0) {
        if (isCity(city)) {
          flags |= FLAG_NO_CITY;
          data.strCity = city;
        } else {
          data.strPlace = city;
        }
      }
      field = p.get();
      parseAddress(StartType.START_ADDR, flags, field, data);
      if (!isValidAddress() && !VALID_ADDR_PTN.matcher(field).matches()) abort();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      field = ' ' + field + ' ';
      int pt = field.indexOf(" X ");
      if (pt < 0) return false;
      if (pt == 0) field = field.substring(3).trim();
      else if (pt == field.length()-3) field = field.substring(0,pt).trim();
      else field = field.substring(0,pt).trim() + " / " + field.substring(pt+3).trim();
      data.strCross = field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
