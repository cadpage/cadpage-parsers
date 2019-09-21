package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class ARGarlandCountyCParser extends DispatchA46Parser {

  public ARGarlandCountyCParser() {
    super("GARLAND COUNTY", "AR");
    }

  @Override
  public String getFilter() {
    return "hotsprings@pagingpts.com>";
  }
}
