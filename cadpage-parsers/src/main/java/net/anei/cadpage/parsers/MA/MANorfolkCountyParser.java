package net.anei.cadpage.parsers.MA;


import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Norfolk County, MA
 */
public class MANorfolkCountyParser extends DispatchSPKParser {
  
  public MANorfolkCountyParser() {
    super("NORFOLK COUNTY","MA");
  }
  
  @Override
  public String getFilter() {
    return "cad@meccdispatch.org";
  }
}
