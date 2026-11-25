package net.anei.cadpage.parsers.ME;

import net.anei.cadpage.parsers.GroupBestParser;


public class MEOxfordCountyParser extends GroupBestParser {

  public MEOxfordCountyParser() {
    super(new MEOxfordCountyAParser(),
          new MEOxfordCountyBParser());
  }
}
