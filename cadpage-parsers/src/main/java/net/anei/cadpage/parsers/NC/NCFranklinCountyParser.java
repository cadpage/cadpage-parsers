

package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Franklin County, NC
 */

public class NCFranklinCountyParser extends DispatchSouthernParser {

  public NCFranklinCountyParser() {
    super(CITY_LIST, "FRANKLIN COUNTY", "NC", 
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_OPT_X|DSFLG_OPT_CODE|DSFLG_TIME);
  }
  
  @Override
  public String getFilter() {
    return "@franklincountync.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern PAREN_NUMBERS_PTN = Pattern.compile("\\(\\d+\\)");
  private static final Pattern TRAIL_PLACE_PTN = Pattern.compile("(?:BETWEEN|JUST) .*", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/").replace(" AT ", "/");
      field = PAREN_NUMBERS_PTN.matcher(field).replaceAll(" ");
      int pt = field.lastIndexOf('/');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        if (isCity(city)) {
          data.strCity = city;
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
      if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
      if (data.strCity.equalsIgnoreCase("HENDESON")) data.strCity = "HENDERSON";
      if (data.strPlace.length() == 0 && TRAIL_PLACE_PTN.matcher(data.strApt).matches()) {
        data.strPlace = data.strApt;
        data.strApt = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE?";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities and Towns
    "BUNN",
    "CENTERVILLE",
    "FRANKLINTON",
    "LOUISBURG",
    "WAKE FOREST", 
    "YOUNGSVILLE",
    
    // Durham County
    "DURHAM",
    
    // Gainville County
    "GAINVILLE CO",
    "GAINVILLE COUNTY",
    "CREEDMOOR",
    "BRASSFIELD",
    "FISHING CREEK",
    "OXFORD",
    "SALEM",
    
    // Halifax County
    "HALIFAX COUNTY",
    "HALIFAX CO",
    "BRINKLEYVILLE",
    "HOLLISTER",
    
    // Johnson County
    "JOHNSON CO",
    "JOHNSON COUNTY",
    "ONEALS",
    
    // Nash County
    "NASH CO",
    "NASH COUNTY",
    "CASTALIA",
    "DRY WELLS",
    "FERRELLS",
    "GRIFFINS",
    "MANNINGS",
    "MIDDLESEX",
    "MOMEYER",
    "SPRING HOPE",
    
    // Vance County
    "VANCE CO",
    "VANCE COUNTY",
    "HENDERSON",
    "HENDESON", // Misspelled
    "KITTRELL",
    
    // Wake County
    "WAKE CO",
    "WAKE COUNTY",
    "FALLS LAKE",
    "LITTLE RIVER",
    "NEW LIGHT",
    "RALEIGH",
    "ROLESVILLE",
    "WAKE FOREST",
    "ZEBULON",
    
    // Warren County
    "WARREN CO",
    "WARREN COUNTY",
    "WARRENTON",
    "SANDY CREEK",
    "SHOCCO",
    "FORK"
   };
  
}
