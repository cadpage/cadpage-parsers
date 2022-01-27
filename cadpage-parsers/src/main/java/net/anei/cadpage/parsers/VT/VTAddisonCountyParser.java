package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Addison County, VT
 */
public class VTAddisonCountyParser extends GroupBestParser {

  public VTAddisonCountyParser() {
    super(new VTAddisonCountyAParser(), new VTAddisonCountyBParser(),
          new VTAddisonCountyCParser(), new VTAddisonCountyDParser());
  }
}
