package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;



public class WIWaukeshaCountyParser extends GroupBestParser {
  public WIWaukeshaCountyParser() {
    super(new WIWaukeshaCountyAParser(), 
          new WIWaukeshaCountyBParser());
  }
}
