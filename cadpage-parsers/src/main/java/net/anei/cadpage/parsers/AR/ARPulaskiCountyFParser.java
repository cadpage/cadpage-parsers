package net.anei.cadpage.parsers.AR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ARPulaskiCountyFParser extends DispatchSouthernParser {

  public ARPulaskiCountyFParser() {
    super(ARPulaskiCountyParser.CITY_LIST, "PULASKI COUNTY", "AR", 
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_X | DSFLG_NAME | DSFLG_PHONE | DSFLG_UNIT1 | DSFLG_ID | DSFLG_TIME);
    setupCities(new String[] {"NLR"});
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equals("NLR")) city = "NORTH LITTLE ROCK";
    return city;
  }

}
