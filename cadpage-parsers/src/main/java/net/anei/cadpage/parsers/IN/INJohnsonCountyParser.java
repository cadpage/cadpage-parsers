package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchC09Parser;

public class INJohnsonCountyParser extends DispatchC09Parser {

  public INJohnsonCountyParser() {
    super("JOHNSON COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "CADVoice";
  }
}
