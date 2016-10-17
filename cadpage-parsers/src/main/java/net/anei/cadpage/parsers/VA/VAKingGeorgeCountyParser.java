package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * King George County, VA
 */
public class VAKingGeorgeCountyParser extends GroupBestParser {
  
  public VAKingGeorgeCountyParser() {
    super(new VAKingGeorgeCountyAParser(), new VAKingGeorgeCountyBParser());
  }
}
