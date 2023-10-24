package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;

public class WIBrownCountyParser extends GroupBestParser {
  public WIBrownCountyParser() {
    super(new WIBrownCountyAParser(),
          new WIBrownCountyBParser(),
          new WIBrownCountyCParser());
  }
}
