package net.anei.cadpage.parsers.SC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class SCChesterCountyParser extends DispatchA71Parser {
 
  public SCChesterCountyParser() {
    super("CHESTER COUNTY", "SC");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MTN_GAP_PTN = Pattern.compile("\\bMTN? GAP\\b", Pattern.CASE_INSENSITIVE);
  @Override
  public String adjustMapAddress(String addr) {
    addr = MTN_GAP_PTN.matcher(addr).replaceAll("MOUNTAIN GAP");
    return addr;
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("CATAWBA")) city = "CHESTER COUNTY";
    return city;
  }
}
