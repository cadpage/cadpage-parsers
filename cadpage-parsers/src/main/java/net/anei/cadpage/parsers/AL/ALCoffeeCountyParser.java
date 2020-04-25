package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALCoffeeCountyParser extends GroupBestParser {
  
  public ALCoffeeCountyParser() {
    super(new ALCoffeeCountyAParser(), new ALCoffeeCountyBParser());
  }
}
