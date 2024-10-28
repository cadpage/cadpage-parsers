package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNPenningtonCountyParser extends GroupBestParser {

  public MNPenningtonCountyParser() {
    super(new MNPenningtonCountyAParser(), new MNPenningtonCountyBParser());
  }
}