package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Jasper County, SC
 */
public class SCJasperCountyParser extends DispatchA9Parser {
      
  public SCJasperCountyParser() {
    super(null,"JASPER COUNTY", "SC");
  }
  
  @Override
  public String getFilter() {
    return "ispatch@jaspercountysc.gov,dispatch@jaspercountysc.gov,";
  }
}
