package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class MIArenacCountyParser extends DispatchEmergitechParser {
  
  public MIArenacCountyParser() {
    super("911:", CITY_LIST, "ARENAC COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "911@arenaccountygov.com";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "AU GRES",
    "AU GRES CITY",
    "AUGRES",
    "AUGRES CITY",
    "OMER",
    "OMER CITY",
    "STANDISH",
    "STANDISH CITY",

    // Villages
    "STERLING",
    "TURNER",
    "TWINING",

    // Unincorporated communities
    "ALGER",
    "DELANO",
    "MAPLE RIDGE",
    "MELITA",
    "PINE RIVER",

    // Townships
    "ADAMS TWP",
    "ARENAC TWP",
    "AU GRES TWP",
    "AUGRES TWP",
    "CLAYTON TWP",
    "DEEP RIVER TWP",
    "LINCOLN TWP",
    "MASON TWP",
    "MOFFATT TWP",
    "SIMS TWP",
    "STANDISH TWP",
    "TURNER TWP",
    "WHITNEY TWP"
  };
}
