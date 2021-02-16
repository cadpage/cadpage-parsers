package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MDPrinceGeorgesCountyIParser extends DispatchH03Parser {

  public MDPrinceGeorgesCountyIParser() {
    super("PRINCE GEORGES COUNTY", "MD");
  }

  @Override
  public String getFilter() {
    return "PSC@co.pg.md.us";
  }
}
