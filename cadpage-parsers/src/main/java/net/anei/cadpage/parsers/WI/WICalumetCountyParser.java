package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;



public class WICalumetCountyParser extends GroupBestParser {
  public WICalumetCountyParser() {
    super(new WICalumetCountyAParser(), new WICalumetCountyBParser(), 
          new WICalumetCountyCParser());
  }
}
