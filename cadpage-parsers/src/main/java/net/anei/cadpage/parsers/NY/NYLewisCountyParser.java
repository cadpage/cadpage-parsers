package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class NYLewisCountyParser extends DispatchB2Parser {

  private static final String[] CITY_LIST = new String[] {
    "CASTORLAND", "CONSTABLEVILLE", "COPENHAGEN",
    "CROGHAN", "DENMARK", "DIANA", "GLENFIELD",
    "GREIG", "HARRISBURG", "HARRISVILLE",
    "HIGHMARKET", "LEWIS", "LEYDEN", "LOWVILLE",
    "LYONS FALLS", "LYONSDALE", "MARTINSBURG",
    "MONTAGUE", "NEW BREMEN", "OSCEOLA", "PINCKNEY",
    "PORT LEYDEN", "TURIN", "WATSON",
    "WEST TURIN", "PITCARIN", "ST LAWRENCE CNTY"};
  
  public NYLewisCountyParser() {
    super("LEWIS911:", CITY_LIST, "LEWIS COUNTY", "NY");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    if(!super.parseMsg(body, data)) return false;
    
    data.strCity = data.strCity.replace("PITCARIN", "PITCAIRN");                    // Fix spelling on Pitcairn
    data.strCity = data.strCity.replace("ST LAWRENCE CNTY", "ST LAWRENCE COUNTY");  // Fix spelling on County
    
    return true;
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }

}
