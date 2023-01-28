package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class LAEvangelineParishParser extends DispatchSPKParser {

  public LAEvangelineParishParser() {
    super("EVANGELINE PARISH", "LA");
  }

  @Override
  public String getFilter() {
    return "vangy911@centurytel.net,alerts@epcd911.org";
  }
}
