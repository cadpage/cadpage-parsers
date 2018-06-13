package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.GroupBestParser;


public class LACalcasieuParishParser extends GroupBestParser {
  
  public LACalcasieuParishParser() {
    super(new LACalcasieuParishAParser(), new LACalcasieuParishBParser());
  }
}
