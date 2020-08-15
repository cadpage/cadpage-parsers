package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA79Parser;

public class CODenverBParser extends DispatchA79Parser {

  public CODenverBParser() {
    super("Call Info", "DENVER", "CO");
  }

  @Override
  public String getFilter() {
    return "dispatch@stadiummedical.com";
  }
}
