package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSMiamiCountyBParser extends DispatchA25Parser {

  public KSMiamiCountyBParser() {
    super("MIAMI COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "paola.mail.scan@paolagov.org";
  }

}
