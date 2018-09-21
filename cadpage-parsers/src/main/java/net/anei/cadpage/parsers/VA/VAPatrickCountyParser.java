package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Patrick County, VA
 */
public class VAPatrickCountyParser extends GroupBestParser {
  
  public VAPatrickCountyParser() {
    super(new VAPatrickCountyAParser(), new VAPatrickCountyBParser());
  }
}
