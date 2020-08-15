package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA79Parser;

public class VADanvilleBParser extends DispatchA79Parser {

  public VADanvilleBParser() {
    super("Trip", "DANVILLE", "VA");
  }

  @Override
  public String getFilter() {
    return "ServerAlerts@dlsc.org";
  }
}
