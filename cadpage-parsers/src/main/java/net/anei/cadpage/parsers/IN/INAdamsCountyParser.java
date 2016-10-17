package net.anei.cadpage.parsers.IN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Adams County, IN
 */
public class INAdamsCountyParser extends DispatchOSSIParser {
  
  public INAdamsCountyParser() {
    super(CITY_LIST, "ADAMS COUNTY", "IN",
           "CALL ADDR/S! CITY? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.adams.in.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    if (OHIO_CITY_LIST.contains(data.strCity)) data.strState = "OH";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strCity = convertCodes(field, CITY_CODES);
    }
  }

  // In county use city codes
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BER", "BERNE",
      "DEC", "DECATUR",
      "MON", "MONROE",
      "GEN", "GENEVA",
      "LIN", "LIN GROVE"      
  });
  
  // out of county use full city/county names
  private static final String[] CITY_LIST = new String[]{
    "WELLS COUNTY",
    "WELLS CO",
    "BLUFFTON",
    "OSSIAN",
    "UNIONDALE",
    "CRAIGVILLE",
    "PONETO",
    "TOCSIN",
    "MARKLE",
    "KEYSTONE",

    "ALLEN COUNTY",
    "ALLEN CO",
    "POE",
    "HOAGLAND",
    "ZANESVILLE",
    "MONROEVILLE",
    "YODER",

    "JAY COUNTY",
    "JAY CO",
    "BRYANT",
    "PORTLAND",
    "PENNVILLE",

    "VAN WERT COUNTY",
    "VAN WERT CO",
    "WILLSHIRE",
    "CONVOY",
    "VAN WERT",

    "MERCER COUNTY",
    "MERCER CO",
    "ROCKFORD"
  };
  
  // Some of whhich are in Ohio
  private Set<String> OHIO_CITY_LIST = new HashSet<String>(Arrays.asList(
    "VAN WERT COUNTY",
    "WILLSHIRE",
    "CONVOY",
    "VAN WERT",

    "MERCER COUNTY",
    "ROCKFORD"
  ));
}
