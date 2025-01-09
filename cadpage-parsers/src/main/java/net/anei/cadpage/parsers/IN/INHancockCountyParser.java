package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INHancockCountyParser extends GroupBestParser {

  public INHancockCountyParser() {
    super(new INHancockCountyBParser(),
          new INHancockCountyCParser(),
          new INHancockCountyDParser());
  }

}
