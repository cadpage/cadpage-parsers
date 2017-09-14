package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WAKlickitatCountyParser extends DispatchA19Parser {
  
  public WAKlickitatCountyParser() {
    super("KLICKITAT COUNTY", "WA");
   }
  
  @Override
  public String getFilter() {
    return "@alert.active911.com,noreply@klickitatcounty.org";
  }
}
