package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ARWhiteCountyParser extends DispatchA71Parser {

  public ARWhiteCountyParser() {
    super("WHITE COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "whitecofire@whiteco911.com";
  }
}
