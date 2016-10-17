package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class VAWinchesterParser extends DispatchA47Parser {
  
  public VAWinchesterParser() {
    super("Winchester ECC info", CITY_LIST, "WINCHESTER", "VA", "[A-Z]{1,3}\\d{1,3}");
  }
  
  @Override
  public String getFilter() {
    return "winecc@ci.winchester.va.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strPlace.endsWith(" RESIDENCE")) {
      data.strName = data.strPlace.substring(0,data.strPlace.length()-10).trim();
      data.strPlace = "";
    }
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("PLACE", "NAME PLACE");
  }
  
  private static final String[] CITY_LIST = new String[]{
    "FREDERICK CO",
    "WINCHESTER"
  };
}
