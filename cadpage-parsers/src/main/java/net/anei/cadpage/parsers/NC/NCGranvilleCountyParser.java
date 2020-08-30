package net.anei.cadpage.parsers.NC;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCGranvilleCountyParser extends DispatchSouthernParser {
  
  public NCGranvilleCountyParser() {
    super(CITY_LIST, "GRANVILLE COUNTY", "NC", 
           DSFLG_PROC_EMPTY_FLDS|DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_ADDR_TRAIL_PLACE|DSFLG_X|DSFLG_NAME|DSFLG_PHONE|DSFLG_CODE|DSFLG_TIME);
    setCallCodePtn("\\d{2}");
  }

  @Override
  public String getFilter() {
    return "@granvillecounty.org";
  }
  
  private static final Pattern NUMBER_DR_PTN = Pattern.compile("# *(\\d+ DR\\b)", Pattern.CASE_INSENSITIVE);
  private static final Pattern NAME_COUNTY_PTN = Pattern.compile("(.*?)[ /]*\\b([A-Z]+) (?:CO|COUNTY|911)", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = NUMBER_DR_PTN.matcher(body).replaceAll("$1");
    if (!super.parseMsg(body, data)) return false;
    if (NUMERIC.matcher(data.strCall).matches()) {
      data.strSupp = append(data.strCall, " ", data.strSupp);
      data.strCall = "";
    }
    
    data.strCity = data.strCity.replace('-', ' ');
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITY_TABLE);

    if (data.strName.length() > 0) {
      data.strName = data.strName.replace('-', ' ');
      Matcher match = NAME_COUNTY_PTN.matcher(data.strName);
      if (match.matches()) {
        data.strName = match.group(1);
        if (data.strCity.length() == 0) data.strCity = match.group(2) + " COUNTY";
      }
      else if (data.strCity.length() == 0 && isCity(data.strName)) {
        data.strCity = data.strName;
        data.strName = "";
      }
    }
    
    if (VA_CITIES.contains(data.strCity)) data.strState = "VA";
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CITY", "CITY ST");
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities and towns
    "BUTNER",
    "CREEDMOOR",
    "CREEMDOOR",  // Misspelled
    "OXFORD",
    "STEM",
    "STOVALL",
    
    // Unincorporated communities
    "BEREA",
    "BRASSFIELD",
    "BRAGGTOWN",
    "BULLOCK",
    "CULBRETH",
    "GRISSOM",
    "LEWIS",
    "SHAKE RAG",
    "SHOOFLY",
    "TALLY HO",
    "WILTON",
    
    // Durham County
    "DURHAM",
    "DURHAM CO",
    "DURHAM-CO",
    "DURHAM COUNTY",
    "BAHAMA",
    "CARR",
    "FALLS LAKE",
    "GORMAN",
    "MANGUM",
    "OAK GROVE",
    "ROUGEMONT",
    
    // Franklin County
    "FRANKLIN",
    "FRANKLIN CO",
    "FRANKLIN-CO",
    "FRANKLIN COUNTY",
    "FRANKLINTON",
    "YOUNGSVILLE",
    
    // Mecklenburg County, VA
    "MECKLENBURG",
    "MECKLENBURG CO",
    "MECKLENBURG-CO",
    
    // Person County
    "PERSON",
    "PERSON CO",
    "PERSON-CO",
    "PERSON COUNTY",
    "ALLENSVILLE",
    "HOLLOWAY",
    "MOUNT TIRZAH",
    "MT TIRZAH",
    "ROXBORO",
    "TIMBERLAKE",
    
    // Vance County
    "VANCE",
    "VANCE CO",
    "VANCE-CO",
    "VANCE COUNTY",
    "TOWNSVILLE",
    "WILLIAMSBORO",
    "DABNEY",
    "WATKINS",
    "KITTRELL",
    
    // Wake County
    "WAKE",
    "WAKE CO",
    "WAKE-CO",
    "WAKE COUNTY",
    "FALLS LAKE",
    "NEW LIGHT",
    "RALEIGH",
    "ROLESVILLE",
    "WAKE FOREST"
  };
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "CREEMDOOR",    "CREEDMOOR"
  });
  
  private static final Set<String> VA_CITIES = new HashSet<String>(
      Arrays.asList(new String[]{
          "MECKLENBURG",
          "MECKLENBURG CO"
      })
  );
}