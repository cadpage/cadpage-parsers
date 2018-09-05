
package net.anei.cadpage.parsers.AL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Morgan County, AL
 */

public class ALMorganCountyParser extends DispatchOSSIParser {
  
  // The program string we want to use and nicely would solve everything is
  // PLACE? ADDR CALL! X? X? ID? UNIT? CH
  // But we can not do anything that simple because we want to avoid relying
  // on address and cross street fields to make decisions.
  //
  // The most reliable indicator is the ID field.  And if it is the 6th field
  // we can reliably conclude that the place and both cross fields are present
  
  // If that doesn't work, we will try to rely on the call description being
  // in position 2 or 3 to identify if the place field is present
  // If that doesn't fly, we will just have to depend on the main address
  // field to determine if we have a place field

  public ALMorganCountyParser() {
    super("MORGAN COUNTY", "AL",
        // The simple program string we want to use is
        // PLACE? ADDR CALL! X? X? ID? UNIT CH
        // But of course that will never work, so we have to do something
        // complicated
        
        // If we can identify the call field, things are pretty simple
        "FYI? ( PLACE ADDR/Z CALL X/Z+? ( ID UNIT | UNIT ) " +
             "| ADDR/Z CALL X/Z+? ( ID UNIT | UNIT ) " +
               
             // If the call field is unknown, things get complicated
             // next see if the unit is in the 7th place, which means all
             // of the optional fields are present
             "| PLACE ADDR/Z CALL/Z X/Z X/Z ID/Z UNIT " +
             
             // Still no luck.  All we can do is hope the address
             // is recognizable
             "| PLACE? ADDR/s CALL! X/Z+? ( ID UNIT | UNIT ) " +
            
             // And finally an optional channel at the end
             ") CH");
  }
  
  @Override
  public String getFilter() {
    return "cad@morgan911.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("UNIT")) return new UnitField("(?!TAC)[A-Z]{3}[A-Z0-9]", true);
    if (name.equals("CH")) return new ChannelField("TAC? ?\\d{0,2}", true);
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!checkCall(field)) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      // If we encounter a 3rd cross street, bail out
      if (data.strCross.contains("&")) abort();
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("MARK SELBY PVT", "SELBY PVT");
    return super.adjustMapAddress(addr);
  }
  
  @Override
  public boolean checkCall(String field) {
    return (CALL_CAT_PTN.matcher(field).matches() ||
            CALL_LIST.contains(field));
    
  }
  
  private static final Pattern CALL_CAT_PTN = Pattern.compile(".* CAT ?\\d");
  private Set<String> CALL_LIST = new HashSet<String>(Arrays.asList(
      "APPLIANCE FIRE",
      "ASSIST LAW ENFORCEMENT",
      "BUSINESS FIRE",
      "BUSINESS FIRE ALARM",
      "BRUSH FIRE",
      "CARBON MONOXIDE ALARM",
      "CAR FIRE",
      "COMMERCIAL VEHICLE FIRE",
      "CONTROLLED BURN",
      "EMERGENCY TRANSPORT",
      "EXPLOSION",
      "FIRE DEPARTMENT CHECK",
      "FIRE OTHER",
      "FIRE PR EVENT",
      "GAS LEAK",
      "GRASS FIRE",
      "HIT AND RUN NO INJURY",
      "HIT AND RUN WITH INJURY",
      "ILLEGAL BURN",
      "LIFT ASSIST",
      "MEDICAL TRANSPORT",
      "PASSENGER VEHICLE FIRE",
      "RESIDENTIAL FIRE",
      "RESIDENTIAL FIRE ALARM",
      "SEARCH AND RESCUE OPERATIONS",
      "SHOOTING",
      "SMOKE INVESTIGATION",
      "STABBING",
      "STRUCTURE FIRE",
      "SUICIDE",
      "SUICIDE ATTEMPTED",
      "SUICIDE THREATENED",
      "TRASH FIRE",
      "URGENT TRANSPORT",
      "WATER RESCUE WITH INJURY",
      "WEATHER RELATED",
      "WRECK NO INJURY",
      "WRECK WITH ENTRAPMENT",
      "WRECK WITH INJURY",
      "WRECK WITH UNKNOWN INJURY"
  ));
}
