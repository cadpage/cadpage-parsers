package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Pelham, AL
 */
public class ALCullmanCountyParser extends DispatchA19Parser {

  public ALCullmanCountyParser() {
    super("CULLMAN COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,donotreply@cullmansheriff.org";
  }
}
