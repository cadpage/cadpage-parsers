package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA87Parser;

public class GAMurrayCountyBParser extends DispatchA87Parser {

  public GAMurrayCountyBParser() {
    super("MURRAY COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "Dispatch@murraycountyga.gov";
  }
}
