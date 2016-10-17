package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IAMuscatineCountyParser extends DispatchA47Parser {
  
  public IAMuscatineCountyParser() {
    super(CITY_LIST, "MUSCATINE COUNTY", "IA", "\\d{3}|[A-Z]{1,3}FD|WLAB");
  }
  
  @Override
  public String getFilter() {
    return "777,888";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.endsWith(" CO")) data.strCity = data.strCity + "UNTY";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities    
    "ATALISSA",
    "BLUE GRASS",
    "DURANT",
    "CONESVILLE",
    "FRUITLAND",
    "MUSCATINE",
    "NICHOLS",
    "STOCKTON",
    "WALCOTT",
    "WEST LIBERTY",
    "WILTON",

    // Unincorporated communities
    "CRANSTON",
    "FAIRPORT",
    "MIDWAY BEACH",
    "MONTPELIER",
    "MOSCOW",
    "PETERSBURG",
    
    // Counties
    "CEDAR CO"
  };
}
