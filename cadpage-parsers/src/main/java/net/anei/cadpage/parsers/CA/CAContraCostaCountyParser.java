package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Contra Costa County, CA
 */
public class CAContraCostaCountyParser extends GroupBestParser {
  public CAContraCostaCountyParser() {
    super(new CAContraCostaCountyAParser(), 
          new CAContraCostaCountyBParser(),
          new CAContraCostaCountyCParser());
  }
}
