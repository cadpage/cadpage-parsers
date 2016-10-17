package net.anei.cadpage.parsers.DE;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Delmar,  DE
 */

public class DEDelmarParser extends FieldProgramParser {

 
  public DEDelmarParser() {
    super("DELMAR", "DE",
           "UNIT! Call_at:ADDR! Loc:PLACE! City:CITY! Problem:CALL! Inc#:ID! Lat:SKIP! Long:SKIP! DISP:TIME! Cross_St:X!");
  }
  
  @Override
  public String getFilter() {
    return "epage-owner@delmar74fire.com,cad@sussexcountyde.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.length() < 95) return false;
    if (!subject.equals("CAD Alert")) return false;
    
    // Support old uncolonated format
    if (body.substring(11,19).equals("Call at ")) {
      
      data.strUnit = body.substring(0,11).trim();
      parseAddress(body.substring(19,49).trim(), data);
      
      int pt = 49;
      int cityPt = body.indexOf("City", pt);
      if (cityPt >= 0) pt = cityPt+4;
      pt = body.indexOf("Problem ", pt);
      if (pt < 0) return false;
      data.strCall = body.substring(pt+8).trim();
      if (cityPt >= 0) {
        data.strCity = body.substring(cityPt+4, pt).trim();
        pt = cityPt;
      }
      data.strPlace = body.substring(49, pt).trim();
      return true;
    } 
    
    // Otherwise we can use the regular field parser
    else {
      body = body.replace("Loc:", " Loc:").replace("City:", " City:").replace("Inc#:", " Inc#:");
      return super.parseMsg(body, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    return super.getField(name);
  }
  
}


