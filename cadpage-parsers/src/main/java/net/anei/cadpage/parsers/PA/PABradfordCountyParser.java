package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA89Parser;

public class PABradfordCountyParser extends DispatchA89Parser {

  public PABradfordCountyParser() {
    super("BRADFORD COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "dispatch@bradfordco911.info";
  }
}
