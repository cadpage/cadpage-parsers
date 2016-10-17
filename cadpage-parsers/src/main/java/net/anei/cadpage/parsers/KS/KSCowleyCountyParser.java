package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Cowley County, KS
 */
public class KSCowleyCountyParser extends DispatchA19Parser {
  
  public KSCowleyCountyParser() {
    super("COWLEY COUNTY", "KS");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
 
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return super.parseMsg(subject, body, data);
  }
}
