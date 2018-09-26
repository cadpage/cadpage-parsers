package net.anei.cadpage.parsers.SC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCGreenvilleCountyBParser extends FieldProgramParser {
  
  public SCGreenvilleCountyBParser() {
    super(CITY_LIST, "GREENVILLE COUNTY", "SC",
          "CALL ADDR! CITY? XINFO END");
  }
  
  @Override
  public String getFilter() {
    return "active911@parkerfd.com,@whfd.org";
  }
  
  private static final Pattern DELIM = Pattern.compile(";|: ");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("<img src=");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!parseFields(DELIM.split(body), data)) return false;
    if (data.strCity.length() == 0) {
      pt = data.strAddress.lastIndexOf(':');
      if (pt >= 0) {
        data.strCity = data.strAddress.substring(pt+1).trim();
        data.strAddress = data.strAddress.substring(0, pt).trim();
        return true;
      }
    }
    return data.strCity.length() > 0 || isPositiveId();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("XINFO")) return new MyCrossInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('.');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyCrossInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
        field = getLeft();
      }
      if (field.startsWith("Between ")) {
        field = field.substring(8).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, field, data);
        field = getLeft();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY X " + super.getFieldNames();
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "FOUNTAIN INN",
      "GREENVILLE",
      "GREER",
      "MAULDIN",
      "SIMPSONVILLE",
      "TRAVELERS REST",

      // Census-designated places
      "BEREA",
      "CITY VIEW",
      "DUNEAN",
      "FIVE FORKS",
      "GANTT",
      "GOLDEN GROVE",
      "JUDSON",
      "PARKER",
      "PIEDMONT",
      "SANS SOUCI",
      "MARIETTA",
      "TAYLORS",
      "TIGERVILLE",
      "WADE HAMPTON",
      "WARE PLACE",
      "WELCOME",

      // Unincorporated communities
      "CLEVELAND",
      "CONESTEE",
      
      ""    // Empty city is valid
      
  };
}
