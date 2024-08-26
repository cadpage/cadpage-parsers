package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ARPulaskiCountyFParser extends DispatchA71Parser {

  public ARPulaskiCountyFParser() {
    super("PULASKI COUNTY", "AR");
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("NLR")) city = "NORTH LITTLE ROCK";
    return city;
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
