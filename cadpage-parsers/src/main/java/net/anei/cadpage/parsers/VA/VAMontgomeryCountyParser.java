package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Montgomery County, VA
 */
public class VAMontgomeryCountyParser extends GroupBestParser {

  public VAMontgomeryCountyParser() {
    super(new VAMontgomeryCountyAParser(),
          new VAMontgomeryCountyBParser(),
          new VAMontgomeryCountyCParser());
  }
}
