package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OKBryanCountyBParser extends DispatchA19Parser {

  public OKBryanCountyBParser() {
    super("BRYAN COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "noreply@durant.org,dispatchnotifier@durant.org";
  }

}
