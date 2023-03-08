package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA92Parser;

public class MIWashtenawCountyBParser extends DispatchA92Parser {

  public MIWashtenawCountyBParser() {
    super("WASHTENAW COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "noreply@emergenthealth.org,cadpaging@emergenthealth.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
