package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNAnokaCountyParser extends GroupBestParser {

  public MNAnokaCountyParser() {
    super(new MNAnokaCountyBParser(), new MNAnokaCountyCParser());
  }
}