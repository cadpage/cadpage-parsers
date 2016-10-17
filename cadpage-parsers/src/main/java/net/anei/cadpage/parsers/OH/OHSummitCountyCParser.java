package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class OHSummitCountyCParser extends DispatchA9Parser {

  public OHSummitCountyCParser() {
    super("SUMMIT COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@police.kent.edu,dispatch@kent.edu";
  }
}
