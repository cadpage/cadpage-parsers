package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSKingmanCountyParser extends DispatchA25Parser {
  
  public KSKingmanCountyParser() {
    super("KINGMAN COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "alert@kingmanlec.kscoxmail.com";
  }
}
