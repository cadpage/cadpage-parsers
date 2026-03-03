package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIPresqueIsleCountyParser extends GroupBestParser {

  public MIPresqueIsleCountyParser() {
    super(new MIPresqueIsleCountyAParser(), new MIPresqueIsleCountyBParser());
  }
}
