package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNHennepinCountyParser extends GroupBestParser {

  public MNHennepinCountyParser() {
    super(new MNHennepinCountyAParser(), new MNHennepinCountyBParser());
  }
}