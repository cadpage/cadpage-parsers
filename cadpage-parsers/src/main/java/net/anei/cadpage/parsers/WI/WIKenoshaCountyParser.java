package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;



public class WIKenoshaCountyParser extends GroupBestParser {
  
  public WIKenoshaCountyParser() {
    super(new WIKenoshaCountyAParser(), 
           new WIKenoshaCountyBParser(),
           new WIKenoshaCountyCParser());
  }
}
