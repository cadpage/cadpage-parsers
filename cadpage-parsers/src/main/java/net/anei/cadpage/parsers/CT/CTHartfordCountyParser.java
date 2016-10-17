package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTHartfordCountyParser extends GroupBestParser {
  
  public CTHartfordCountyParser() {
    super(new CTHartfordCountyAvonParser(), new CTHartfordCountyEastGranbyParser(),
        new CTHartfordCountyFarmingtonParser());
  }
}
