package net.anei.cadpage.parsers.SD;

import net.anei.cadpage.parsers.GroupBestParser;

public class SDMinnehahaCountyParser extends GroupBestParser {

  public SDMinnehahaCountyParser() {
    super(new SDMinnehahaCountyAParser(),
          new SDMinnehahaCountyBParser(),
          new SDMinnehahaCountyCParser());
  }
}
