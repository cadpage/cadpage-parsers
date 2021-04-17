package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Rockingham County, VA
 */
public class VARockinghamCountyParser extends GroupBestParser {

  public VARockinghamCountyParser() {
    super(new VARockinghamCountyAParser(),
          new VARockinghamCountyBParser(),
          new VARockinghamCountyCParser());
  }
}
