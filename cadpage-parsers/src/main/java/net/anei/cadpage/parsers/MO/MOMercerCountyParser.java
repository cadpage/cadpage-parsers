package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOMercerCountyParser extends DispatchA33Parser {

  public MOMercerCountyParser() {
    super("MERCER COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "hcso@omnigo.com";
  }

}
