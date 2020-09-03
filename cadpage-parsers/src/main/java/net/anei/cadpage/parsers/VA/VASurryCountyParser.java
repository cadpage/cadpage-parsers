package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Surry County, VA
 */
public class VASurryCountyParser extends GroupBestParser {
  
  public VASurryCountyParser() {
    super(new VASurryCountyAParser(), new VASurryCountyBParser());
  }
}
