package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSGrayCountyParser extends DispatchA25Parser {

  public KSGrayCountyParser() {
    super("GRAY COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "gysoenterpol@grayco.org";
  }

}
