package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDTwinFallsCountyParser extends GroupBestParser {
  
  public IDTwinFallsCountyParser() {
   super(new IDTwinFallsCountyAParser(), new IDTwinFallsCountyBParser());
  }
}
