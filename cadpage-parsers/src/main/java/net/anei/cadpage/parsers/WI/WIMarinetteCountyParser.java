package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;



public class WIMarinetteCountyParser extends GroupBestParser {

  public WIMarinetteCountyParser() {
    super(new WIMarinetteCountyAParser(),
           new WIMarinetteCountyBParser(),
           new WIMarinetteCountyCParser());
  }
}
