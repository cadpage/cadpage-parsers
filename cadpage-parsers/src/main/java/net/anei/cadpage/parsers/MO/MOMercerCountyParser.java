package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOMercerCountyParser extends DispatchBCParser {
  
  public MOMercerCountyParser() {
    super("MERCER COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "hcso@omnigo.com";
  }

}
