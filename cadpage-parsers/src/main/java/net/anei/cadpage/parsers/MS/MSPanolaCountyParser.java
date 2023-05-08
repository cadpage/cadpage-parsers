package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.GroupBestParser;


public class MSPanolaCountyParser extends GroupBestParser {

  public MSPanolaCountyParser() {
    super(new MSPanolaCountyAParser(), new MSPanolaCountyBParser());
  }
}
