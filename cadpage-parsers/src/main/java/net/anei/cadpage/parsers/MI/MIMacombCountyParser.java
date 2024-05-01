package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIMacombCountyParser extends GroupBestParser {

  public MIMacombCountyParser() {
    super(new MIMacombCountyAParser(), new MIMacombCountyBParser());
  }
}
