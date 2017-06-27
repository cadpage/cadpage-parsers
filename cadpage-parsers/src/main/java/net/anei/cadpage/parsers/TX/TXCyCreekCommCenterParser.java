package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * CyCreek Comm Center, TX
 */
public class TXCyCreekCommCenterParser extends GroupBestParser {
  
  public TXCyCreekCommCenterParser() {
    super(new TXCyCreekCommCenterAParser(), new TXCyCreekCommCenterBParser());
  }
}
