package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Rockbridge County, VA
 */
public class VARockbridgeCountyParser extends GroupBestParser {

  public VARockbridgeCountyParser() {
    super(new VARockbridgeCountyAParser(),
          new VARockbridgeCountyBParser(),
          new VARockbridgeCountyCParser());
  }
}
