package net.anei.cadpage.parsers.CO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class COLarimerCountyDParser extends DispatchA41Parser {
  
  public   COLarimerCountyDParser() {
    super(CITY_CODES, "LARIMER COUNTY", "CO", "[A-Z]{1,2}FD"); 
  }
  
  @Override
  public String getFilter() {
    return "crisppaging@fcgov.com,mregan@poudre-fire.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    do {
      if (subject.equals("CAD Page")) break;
      
      if (body.startsWith("/ CAD Page / ")) {
        body = body.substring(13);
        break;
      }
      
      return false;
    } while (false);
    
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FTC", "FORT COLLINS",
      "LFC", "FORT COLLINS",
      "LLV", "LOVELAND",
      "LOV", "LOVELAND",
      "TIM", "TIMNATH",
      "WEL", "WELLINGTON",
      "WLD", "WELD COUNTY",

  });
}
