package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Orange County, VT
 */
public class VTOrangeCountyParser extends GroupBestParser {

  public VTOrangeCountyParser() {
    super(new VTOrangeCountyAParser(), new VTOrangeCountyBParser());
  }
}
