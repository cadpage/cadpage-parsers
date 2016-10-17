package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAFauquierCountyParser extends DispatchOSSIParser {
  
  public VAFauquierCountyParser() {
    super("Fauquier County", "VA",
        "BOX? CALL! ADDR! X/Z+? UNIT CH");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Calls with no box number are OOC mutual aid calls
    if (data.strBox.length() == 0) data.defCity = "";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("\\d[A-Z0-9]{3}|[A-Z]\\d{3}");
    if (name.equals("UNIT")) return new UnitField("(?:CO|ST)\\d+");
    return super.getField(name);
  }
}

