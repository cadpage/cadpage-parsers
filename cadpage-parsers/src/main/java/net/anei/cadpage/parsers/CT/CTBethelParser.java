package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTBethelParser extends GroupBestParser {

  public CTBethelParser() {
    super(new CTBethelAParser(), new CTBethelBParser());
  }
}
