package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALPikeCountyParser extends GroupBestParser {

  public ALPikeCountyParser() {
    super(new ALPikeCountyAParser(), new ALPikeCountyBParser());
  }
}
