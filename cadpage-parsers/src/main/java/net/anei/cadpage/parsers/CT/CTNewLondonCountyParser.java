package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTNewLondonCountyParser extends GroupBestParser {

  public CTNewLondonCountyParser() {
    super(new CTNewLondonCountyAParser(),
          new CTNewLondonCountyBParser(),
          new CTNewLondonCountyCParser(),
          new CTNewLondonCountyDParser(),
          new CTNewLondonCountyEParser());
  }
}
