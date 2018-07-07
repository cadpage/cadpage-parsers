package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Windham County, VT
 */
public class VTWindhamCountyParser extends GroupBestParser {
  
  public VTWindhamCountyParser() {
    super(new VTWindhamCountyAParser(), new VTWindhamCountyBParser());
  }
}
