package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/**
 * Prince Edward County, VA
 */
public class VAPrinceEdwardCountyParser extends GroupBestParser {
  
  public VAPrinceEdwardCountyParser() {
    super(new VAPrinceEdwardCountyBParser(),
          new GroupBlockParser(),
          new VAPrinceEdwardCountyAParser());
  }
}
