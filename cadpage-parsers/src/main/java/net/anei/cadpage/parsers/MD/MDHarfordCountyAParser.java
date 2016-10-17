package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;


public class MDHarfordCountyAParser extends DispatchBParser {

  public MDHarfordCountyAParser() {
    super(CITY_LIST, "HARFORD COUNTY", "MD");
  }
  
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    
    // First token following EOC: is the unit
    if (body.startsWith("HCCAD /")) body = body.substring(7).trim();
    if (body.startsWith("[!]")) body = body.substring(3).trim();
    if (! body.startsWith("EOC:")) return false;
    body = body.substring(4).trim();
    int pt = body.indexOf(' ');
    if (pt < 0) return false;
    data.strUnit = body.substring(0, pt);
    body = body.substring(pt+1).trim();
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strCross.startsWith("X ")) data.strCross = data.strCross.substring(2);
    
    if (!data.strName.equals("NOT ENTERED")) {
      if (data.strName.startsWith("LIFE ALERT")) {
        data.strSupp = data.strName;
        data.strName = "";
      }
      pt = data.strName.lastIndexOf(' ');
      if (data.strName.length()-pt < 4) pt = data.strName.lastIndexOf(' ', pt-1);
      if (pt > 0) {
        data.strPlace = data.strName.substring(0,pt).trim();
        data.strName = data.strName.substring(pt+1);
        
        if (data.strPlace.startsWith("X ")) {
          data.strCross = append(data.strCross, " & ", data.strPlace.substring(2).trim());
          data.strPlace = "";
        }
        else if (isValidAddress(data.strPlace)) {
          data.strCross = append(data.strCross, " & ", data.strPlace);
          data.strPlace = "";
        }
      }
    }
    return true;
  }
  
  private static final String[] CITY_LIST =new String[]{
    "ABERDEEN", 
    "BEL AIR", 
    "HAVRE DE GRACE", 
    "JOPPA", 
    "ABINGDON", 
    "BELCAMP",
    "LEVEL", 
    "CHURCHVILLE", 
    "DARLINGTON", 
    "WHITEFORD", 
    "JARRETTSVILLE", 
    "STREET",
    "HICKORY", 
    "FALLSTON", 
    "JOPPATOWNE", 
    "FAWN GROVE", 
    "DELTA", 
    "PYLESVILLE", 
    "WHITE HALL", 
    "FOREST HILL"
  };
}
