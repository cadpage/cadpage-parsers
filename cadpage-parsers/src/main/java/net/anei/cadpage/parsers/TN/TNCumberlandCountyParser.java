package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNCumberlandCountyParser extends GroupBestParser {

  public TNCumberlandCountyParser() {
    super(new TNCumberlandCountyAParser(), new TNCumberlandCountyBParser());
  }
}
