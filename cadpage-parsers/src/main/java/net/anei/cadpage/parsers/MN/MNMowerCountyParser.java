package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNMowerCountyParser extends GroupBestParser {

  public MNMowerCountyParser() {
    super(new MNMowerCountyAParser(), new MNMowerCountyBParser());
  }
}