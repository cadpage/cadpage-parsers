package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Geauga County, OH
 */

public class OHGeaugaCountyParser extends GroupBestParser {
  
  public OHGeaugaCountyParser() {
    super(new OHGeaugaCountyAParser(), new OHGeaugaCountyBParser());
  }
}
