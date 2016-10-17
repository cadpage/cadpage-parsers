package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA8Parser;




public class NYOswegoCountyAParser extends DispatchA8Parser {
  
  public NYOswegoCountyAParser() {
    super(CITY_CODES, "OSWEGO COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "firereporting@westmonroefire.org";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body,  data)) return false;
    
    if (data.strApt.length() == 0) {
      Matcher match = PLACE_APT_PTN.matcher(data.strPlace);
      if (match.find()) {
        data.strApt = match.group();
        data.strPlace = data.strPlace.substring(match.end()).trim();
        if (data.strPlace.startsWith(",")) data.strPlace = data.strPlace.substring(1).trim();
      }
    }
    
    if (data.strCity.length() == 0) {
      if (data.strName.endsWith(" COUNTY")) {
        data.strCity = data.strName;
        data.strName = "";
      }
    }
    
    return true;
  }
  private static final Pattern PLACE_APT_PTN = Pattern.compile("^(?:LOT|APT|ROOM|RM):? *[^ ]+", Pattern.CASE_INSENSITIVE);
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(stripCityCode(field), data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      super.parse(stripCityCode(field), data);
    }
  }
  
  private String stripCityCode(String field) {
    field = ADDR_CITY_PTN.matcher(field).replaceAll(" ").trim();
    return field;
  }
  private static final Pattern ADDR_CITY_PTN = Pattern.compile(", [A-Z]{3}\\b");
  
  protected void parseSpecialField(String field, Data data) {
    Parser p = new Parser(field);
    String cross = getPatternValue(p, CROSS_PTN);
    if (cross.startsWith("/")) cross = cross.substring(2).trim();
    if (cross.endsWith("/")) cross = cross.substring(0,cross.length()-1).trim();
    data.strCross = cross;
    
    Matcher match = p.getMatcher(UNIT_SRC_PTN);
    if (match != null) {
      data.strUnit = match.group(1).trim();
      data.strSource = match.group(2).trim().replace("FIRE DEPARTMENT", "FD");
    }
    data.strCallId = getPatternValue(p, ID_PTN);
    data.strSupp = append(data.strSupp, "\n", getPatternValue(p, INFO_PTN));
    
  }
  private static final Pattern CROSS_PTN = Pattern.compile("\nLocation:.*\\(([^\n\\)]+)\\)");
  private static final Pattern UNIT_SRC_PTN = Pattern.compile("Handling Unit: (.*)Agency Name: (.*)\n");
  private static final Pattern ID_PTN = Pattern.compile("\nCase Number: *(.*)\n");
  private static final Pattern INFO_PTN = Pattern.compile("(Incident Notes.*)", Pattern.DOTALL);
  
  private String getPatternValue(Parser p, Pattern ptn) {
    Matcher match = p.getMatcher(ptn);
    return (match == null ? "" : match.group(1).trim());
  }
  
  protected String specialFieldNames() {
    return "X UNIT SRC ID INFO";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
  
  @Override
  public String postAdjustMapAddress(String sAddress) {
    // In this county, County Routes really should be county routes.
    return sAddress.replace("COUNTY ROAD", "COUNTY ROUTE");
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLE", "CLEVELAND",
      "CON", "CONSTANTIA",
      "HAS", "HASTINGS",
      "PAR", "PARISH",
      "SCH", "SCHROEPPEL",
      "WMN", "WEST MONROE",
      "WMR", "WEST MONROE"
  });
}
	