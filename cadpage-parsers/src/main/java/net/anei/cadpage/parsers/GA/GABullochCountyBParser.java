package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA36Parser;


public class GABullochCountyBParser extends DispatchA36Parser {
 
  public GABullochCountyBParser() {
    super("BULLOCH COUNTY", "GA", 2);
  }
  
  @Override
  public String getFilter() {
    return "bullochga@ez911mail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
 }
