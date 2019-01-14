package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Prince Edward County, VA
 */
public class VAPrinceEdwardCountyParser extends GroupBestParser {
  
  public VAPrinceEdwardCountyParser() {
    super(new VAPrinceEdwardCountyAParser(), new VAPrinceEdwardCountyBParser());
  }
}
