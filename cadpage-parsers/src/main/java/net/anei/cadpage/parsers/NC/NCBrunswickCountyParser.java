package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCBrunswickCountyParser extends GroupBestParser {
  
  public NCBrunswickCountyParser() {
    super(new NCBrunswickCountyAParser(), new NCBrunswickCountyBParser(),
          new NCBrunswickCountyCParser());
  }
}
