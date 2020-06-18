package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Louisa County, VA
 */
public class VALouisaCountyParser extends GroupBestParser {
  
  public VALouisaCountyParser() {
    super(new VALouisaCountyAParser(), new VALouisaCountyBParser());
  }
}
