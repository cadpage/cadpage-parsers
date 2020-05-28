package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

public class FLOrangeCountyAParser extends DispatchA37Parser {

  public FLOrangeCountyAParser() {
    super("WinterParkFireDepartmentDispatch", CITY_LIST, "ORANGE COUNTY", "FL");
  }
  
  @Override
  public String getFilter() { 
    return "WinterParkFireDepartmentDispatch@cityofwinterpark.org"; 
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  protected boolean parseLocationField(String field, Data data) {
    if (field.endsWith(" FL")) {
      data.strState = "FL";
      field = field.substring(0,field.length()-3).trim();
    }
    return super.parseLocationField(field, data);
  }

  @Override
  protected boolean parseMessageField(String field, Data data) {
    
    data.strSupp = field;
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
      "ORANGE COUNTY",

      //CITIES
      "APOPKA",
      "BAY LAKE",
      "BELLE ISLE",
      "EDGEWOOD",
      "LAKE BUENA VISTA",
      "MAITLAND",
      "OCOEE",
      "ORLANDO",
      "WINTER GARDEN",
      "WINTER PARK"
  };
}
