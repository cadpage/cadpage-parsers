package net.anei.cadpage.parsers.CO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA59Parser;

public class COKitCarsonCountyParser extends DispatchA59Parser {
  
  public COKitCarsonCountyParser() {
    super(CITY_LIST, "KIT CARSON COUNTY", "CO");
    removeWords("X");
  }
  
  @Override
  public String getFilter() {
    return "kccdispatch@yahoo.com";
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    return HWY_385_PTN.matcher(sAddress).replaceAll("US 385");
  }
  
  private static final Pattern HWY_385_PTN = Pattern.compile("\\bHWY *385\\b");

  private static final String[] CITY_LIST = new String[] { 
    "BETHUNE",
    "BURLINGTON",
    "FLAGLER",
    "SEIBERT",
    "STRATTON",
    "VONA"
  };
}
