package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OKSandSpringsParser extends DispatchA19Parser {

  public OKSandSpringsParser() {
    super("SAND SPRINGS", "OK");
  }

  @Override
  public String getFilter() {
    return "@sandspringsok.org,@sandspringsok.gov";
  }
}
