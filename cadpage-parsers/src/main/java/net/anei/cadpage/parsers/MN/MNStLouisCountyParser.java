package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNStLouisCountyParser extends GroupBestParser {

  public MNStLouisCountyParser() {
    super(new MNStLouisCountyAParser(), new MNStLouisCountyBParser());
  }
}