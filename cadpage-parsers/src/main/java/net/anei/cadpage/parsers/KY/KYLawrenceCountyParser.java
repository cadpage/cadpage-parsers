package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYLawrenceCountyParser extends DispatchB2Parser {
  
  public KYLawrenceCountyParser() {
    super("LAWRENCE_911:",CITY_LIST, "LAWRENCE COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "LAWRENCE_911@lycomonline.com,Interact@lycomonline.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.endsWith(" WAYNE CO")) {
      data.strCity = data.strCity.substring(0,data.strCity.length()-9).trim();
      data.strState = "WV";
    }
    data.strName = stripFieldEnd(data.strName, "APPALACHIAN WIRELESS");
    data.strAddress = stripFieldEnd(data.strAddress, "APPALACHIAN WIRELESS");
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final String[] CITY_LIST = new String[]{
    "BLAINE",
    "LOUISA",
    "LOWMANSVILLE",
    "FALLSBURG",
    "WEBBVILLE",
    "KISE",
    "CHERRYVILLE",
    "ULYSSES",

    "FT GAY WAYNE CO"
    };


}
