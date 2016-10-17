package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Lee County, NC
 */
public class NCLeeCountyParser extends DispatchOSSIParser {

  public NCLeeCountyParser() {
    super("LEE COUNTY", "NC",
          "FYI? UNIT CALL ADDR! X X INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@sanfordnc.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("CAD:")) {
      if (!subject.equals("Message Forwarded by PageGate")) return false;
      body = "CAD:" + body;
    }

    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z][A-Z0-9]+", true);
    return super.getField(name);
  }
}
