package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Hamilton County, OH
 */

public class OHHamiltonCountyParser extends GroupBestParser {

  public OHHamiltonCountyParser() {
    super(new OHHamiltonCountyAParser(), new OHHamiltonCountyBParser(),
          new OHHamiltonCountyCParser(),
          new OHCincinnatiParser());
  }

  @Override
  public String getLocName() {
    return "Hamilton County, OH";
  }
}
