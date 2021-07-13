package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Caledonia County, VT
 */
public class VTCaledoniaCountyParser extends GroupBestParser {

  public VTCaledoniaCountyParser() {
    super(new VTCaledoniaCountyAParser(), new VTCaledoniaCountyBParser());
  }
}
