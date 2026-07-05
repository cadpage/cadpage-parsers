package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOMississippiCountyBParser extends DispatchBCParser {

  public MOMississippiCountyBParser() {
    super("MISSISSIPPI COUNTY","MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
