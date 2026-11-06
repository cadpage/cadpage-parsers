package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MNCrowWingCountyCParser extends DispatchA19Parser {

  public MNCrowWingCountyCParser() {
    super("CROW WING COUNTY", "MN");
  }

  @Override
  public String getFilter() {
    return "ibridge@mail.police365.com";
  }

}
