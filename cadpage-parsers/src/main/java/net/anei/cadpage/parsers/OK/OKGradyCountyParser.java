package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class OKGradyCountyParser extends DispatchA72Parser {

  public OKGradyCountyParser() {
    super("GRADY COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "noreplycad@cityoftuttle.com,noreplycad@tuttleok.gov";
  }
}
