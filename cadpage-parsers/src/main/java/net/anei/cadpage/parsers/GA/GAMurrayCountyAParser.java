package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class GAMurrayCountyAParser extends DispatchA9Parser {
  
  public  GAMurrayCountyAParser() {
    super("MURRAY COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "newworldadmin@murraycountyga.gov";
  }

}
