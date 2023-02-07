package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCGuilfordCountyParser extends GroupBestParser {

  public NCGuilfordCountyParser() {
    super(new NCGuilfordCountyAParser(), new NCGuilfordCountyBParser(),
          new NCGuilfordCountyDParser());
  }
}
