package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Burke County, NC
 */
public class NCBurkeCountyParser extends DispatchOSSIParser {
  
  public NCBurkeCountyParser() {
    super("BURKE COUNTY", "NC",
           "( CANCEL ADDR | SRC CALL CODE? ADDR! X? X? ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@bceoc.org,CAD@burke.local";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("|")) body = body.substring(1).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    if (name.equals("CODE")) return new CodeField("\\d\\d[A-Z]\\d\\d[A-Za-z]?", true);
    return super.getField(name);
  }
}
