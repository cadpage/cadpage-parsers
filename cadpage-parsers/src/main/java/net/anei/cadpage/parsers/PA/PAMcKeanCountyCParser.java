package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class PAMcKeanCountyCParser extends DispatchA57Parser {

  public PAMcKeanCountyCParser() {
    super("MCKEAN COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "noreply@ntr911sa.com";
  }
}
