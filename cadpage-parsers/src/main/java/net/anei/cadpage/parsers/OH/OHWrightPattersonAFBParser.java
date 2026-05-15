package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class OHWrightPattersonAFBParser extends DispatchA72Parser {

  public OHWrightPattersonAFBParser() {
    super("WRIGHT-PATTERSON AFB", "OH");
  }

  @Override
  public String getFilter() {
    return "wpafbfddispatch@gmail.com";
  }

}
