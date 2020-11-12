package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAElbertCountyParser extends DispatchSPKParser {

  public GAElbertCountyParser() {
    super("ELBERT COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "elbertco911@gmail.com";
  }
}
