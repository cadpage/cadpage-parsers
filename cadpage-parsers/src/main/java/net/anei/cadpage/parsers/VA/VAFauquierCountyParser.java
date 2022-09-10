package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAFauquierCountyParser extends DispatchOSSIParser {
  
  public VAFauquierCountyParser() {
    super("Fauquier County", "VA",
        "BOX? CALL! UNIT? ADDR! X/Z+? SRC CH");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    
    // Calls with no box number are OOC mutual aid calls
    if (data.strBox.length() == 0) data.defCity = "";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("[-/, A-Z0-9]+", true);
    if (name.equals("BOX")) return new BoxField("\\d[A-Z0-9]{3}|[A-Z]\\d{3}", true);
    if (name.equals("UNIT")) return new UnitField("[^ ]+", true);
    if (name.equals("SRC")) return new SourceField("(?:CO|ST)\\d+|\\d{4}", true);
    return super.getField(name);
  }
}

