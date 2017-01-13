package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSSumnerCountyParser extends DispatchA25Parser {
  
  public KSSumnerCountyParser() {
    super("SUMNER COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "sumner911cad@co.sumner.ks.us";
  }
}
