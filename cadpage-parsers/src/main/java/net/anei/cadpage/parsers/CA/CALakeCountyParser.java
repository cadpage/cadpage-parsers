package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lake County, CA
 */
public class CALakeCountyParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Dispatched Call \\(([A-Z]+)\\)");
  private static final Pattern E911_INFO_PTN = Pattern.compile("(?:\n+Cellular E911 Call: \nLat:([-+]?\\d+\\.\\d+) +Lon:([-+]?\\d+\\.\\d+))?\nService Class: .*$");
  private static final Pattern DELIM = Pattern.compile("(?<= )\\*(?= )");
  
  public CALakeCountyParser() {
    super(CITY_CODES, "LAKE COUNTY", "CA",
           "ADDR PLACE X APT CITY? CALL! APT ID? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "lakecounty.dispatch@lakecountyca.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return D_ALBERT_PTN.matcher(addr).replaceAll("D ALBERT");
  }
  private static final Pattern D_ALBERT_PTN = Pattern.compile("\\bD'ALBERT\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strSource = match.group(1);
    
    match = E911_INFO_PTN.matcher(body);
    if (match.find()) {
      String lat = match.group(1);
      String lon = match.group(2);
      if (lat != null) setGPSLoc(lat+','+lon, data);
      body = body.substring(0,match.start()).trim();
    }
    
    return parseFields(DELIM.split(body), 5, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " GPS";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("CALL")) return new MyCallCode();
    if (name.equals("ID")) return new IdField("#(\\d+)", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.equals("CA")) city = p.getLastOptional(',');
      data.strCity = city;
      field = p.get();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  // City field doesn't exist if it was entered as part of the address
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) return false;
      return super.checkParse(field, data);
    }
  }
  
  // Call field expands call codes
  private class MyCallCode extends CallField {
    @Override
    public void parse(String field, Data data) {
      String desc = CALL_CODES.getProperty(field);
      if (desc != null) {
        field = field + " - " + desc;
      }
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CB", "Cobb",
      "CL", "Clearlake",
      "CO", "Clearlake Oaks",
      "CP", "Clearlake Park",
      "FI", "Finley",
      "GL", "Glenhaven",
      "HV", "Hidden Valley Lake",
      "KV", "Kelseyville",
      "LK", "Lake Pillsbury",
      "LL", "Lower Lake",
      "LO", "Loch Lomond",
      "LP", "Lakeport",
      "LU", "Lucerne",
      "MI", "Middletown",
      "NI", "Nice",
      "UL", "Upper Lake",
      "WP", "Whispering Pines",
      "WS", "Witter Springs",
  });
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "FDAA", "Auto Accident",
      "FDBT", "Bomb Threat",
      "FDAR", "Alarm Sounding",
      "FDHM", "Haz-Mat",
      "FDMA", "Medical Aid",
      "FDSF", "Structure Fire",
      "FDSC", "Smoke Check",
      "FDPA", "Public Assist",
      "FDLL", "Life Line Alert",
      "FDLD", "Lines Down",
      "FDVF", "Vehicle Fire",
      "FDWF", "Wildland Fire",
      "FDWR", "Water Rescue",
      "FDCF", "Chimney Fire",
      "FDAF", "Appliance Fire",
      "FDOE", "Other Event",
      "FDWI", "Walk-In",
      "FDWC", "Welfare Check",
  });
}
