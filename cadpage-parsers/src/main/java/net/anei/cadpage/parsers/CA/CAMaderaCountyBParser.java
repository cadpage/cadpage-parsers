package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAMaderaCountyBParser extends DispatchA20Parser {
  
  public CAMaderaCountyBParser() {
    super("MADERA COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "@cityofchowchilla.org";
  }

}
