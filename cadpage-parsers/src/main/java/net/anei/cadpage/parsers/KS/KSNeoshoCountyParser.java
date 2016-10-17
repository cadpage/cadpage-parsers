package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class KSNeoshoCountyParser extends DispatchA33Parser {
  
  public KSNeoshoCountyParser() {
    super("NEOSHO COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "NEOSHO911@ACEKS.COM";
  }
  
 }
