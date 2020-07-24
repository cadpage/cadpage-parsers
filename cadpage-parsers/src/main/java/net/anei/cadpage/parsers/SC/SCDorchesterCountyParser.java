package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCDorchesterCountyParser extends GroupBestParser {

  public SCDorchesterCountyParser() {
    super(new SCDorchesterCountyAParser(), new SCDorchesterCountyBParser());
  }
}