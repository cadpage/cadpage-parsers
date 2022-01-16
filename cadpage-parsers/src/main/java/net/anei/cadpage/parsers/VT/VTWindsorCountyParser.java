package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Windsor County, VT
 */
public class VTWindsorCountyParser extends GroupBestParser {

  public VTWindsorCountyParser() {
    super(new VTWindsorCountyAParser(),
          new VTWindsorCountyBParser(),
          new VTWindsorCountyCParser());
  }
}
