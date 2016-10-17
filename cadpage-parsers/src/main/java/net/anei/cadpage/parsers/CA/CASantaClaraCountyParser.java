package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Santa Clara, CA
 */
public class CASantaClaraCountyParser extends GroupBestParser {
  public CASantaClaraCountyParser() {
    super(new CASantaClaraCountyAParser(), new CASantaClaraCountyBParser());
  }
}
