package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNHubbardCountyParser extends GroupBestParser {

  public MNHubbardCountyParser() {
    super(new MNHubbardCountyAParser(), new MNHubbardCountyBParser());
  }
}