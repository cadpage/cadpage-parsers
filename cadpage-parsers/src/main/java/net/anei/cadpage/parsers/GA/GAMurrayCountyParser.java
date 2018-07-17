package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class GAMurrayCountyParser extends DispatchA9Parser {
  
  public  GAMurrayCountyParser() {
    super("MURRAY COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "newworldadmin@murraycountyga.gov";
  }

}
