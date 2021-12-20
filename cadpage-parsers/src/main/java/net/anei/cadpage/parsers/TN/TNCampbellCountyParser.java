package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNCampbellCountyParser extends GroupBestParser {

  public TNCampbellCountyParser() {
    super(new TNCampbellCountyAParser(), new TNCampbellCountyBParser());
  }
}
