package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class SCLexingtonCountyBParser extends DispatchH03Parser {

  public SCLexingtonCountyBParser() {
    super("LEXINGTON COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return ".CD@P1CAD.CommandCentral.com";
  }
}
