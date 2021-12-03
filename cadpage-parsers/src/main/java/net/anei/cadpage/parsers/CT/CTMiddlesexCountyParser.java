package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTMiddlesexCountyParser extends GroupBestParser {

  public CTMiddlesexCountyParser() {
    super(new CTMiddlesexCountyAParser(),
          new CTMiddlesexCountyBParser(),
          new CTMiddlesexCountyCParser(),
          new CTMiddlesexCountyDParser());
  }
}
