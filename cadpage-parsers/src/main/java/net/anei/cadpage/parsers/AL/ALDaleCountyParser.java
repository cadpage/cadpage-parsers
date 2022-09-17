package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALDaleCountyParser extends GroupBestParser {

  public ALDaleCountyParser() {
    super(new ALDaleCountyAParser(), new ALDaleCountyBParser());
  }
}
