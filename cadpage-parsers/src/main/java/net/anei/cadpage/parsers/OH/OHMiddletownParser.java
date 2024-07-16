package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class OHMiddletownParser extends DispatchA71Parser {

  public OHMiddletownParser() {
    super("MIDDLETOWN", "OH");
  }

  @Override
  public String getFilter() {
    return "cad@cityofmiddletown.org";
  }

}
