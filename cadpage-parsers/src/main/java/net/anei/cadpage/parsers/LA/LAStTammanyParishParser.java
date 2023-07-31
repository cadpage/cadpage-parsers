package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.GroupBestParser;


public class LAStTammanyParishParser extends GroupBestParser {

  public LAStTammanyParishParser() {
    super(new LAStTammanyParishAParser(),
          new LAStTammanyParishBParser(),
          new LAStTammanyParishCParser());
  }
}
