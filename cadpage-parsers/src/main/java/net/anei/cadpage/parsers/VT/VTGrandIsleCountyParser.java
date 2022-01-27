package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * GrandIsle County, VT
 */
public class VTGrandIsleCountyParser extends GroupBestParser {

  public VTGrandIsleCountyParser() {
    super(new VTGrandIsleCountyAParser(), new VTGrandIsleCountyBParser(),
          new VTGrandIsleCountyCParser());
  }
}
