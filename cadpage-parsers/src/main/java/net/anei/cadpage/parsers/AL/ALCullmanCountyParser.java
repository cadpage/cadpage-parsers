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
    return "25064-KS3YajNtu7AasQVa@alert.active911.com";
  }
}
