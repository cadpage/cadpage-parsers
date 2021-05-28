package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA70Parser;

public class KYLaurelCountyParser extends DispatchA70Parser {

  public KYLaurelCountyParser() {
    super("LAUREL COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
