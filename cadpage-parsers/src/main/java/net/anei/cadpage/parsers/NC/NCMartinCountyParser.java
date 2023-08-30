package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCMartinCountyParser extends GroupBestParser {

  public NCMartinCountyParser() {
    super(new NCMartinCountyAParser(),
          new NCMartinCountyBParser(),
          new NCMartinCountyCParser());
  }
}
