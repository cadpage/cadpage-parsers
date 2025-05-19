package net.anei.cadpage.parsers.NV;

import net.anei.cadpage.parsers.GroupBestParser;



public class NVClarkCountyParser extends GroupBestParser {

  public NVClarkCountyParser() {
    super(new NVClarkCountyAParser(),
          new NVClarkCountyBParser(),
          new NVClarkCountyCParser(),
          new NVClarkCountyDParser(),
          new NVClarkCountyEParser());
  }
}
