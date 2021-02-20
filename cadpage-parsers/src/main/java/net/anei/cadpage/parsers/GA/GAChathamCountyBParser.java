package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class GAChathamCountyBParser extends DispatchA72Parser {

  public GAChathamCountyBParser() {
    super("CHATHAM COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "dispatch@cityoftybee.org";
  }

}
