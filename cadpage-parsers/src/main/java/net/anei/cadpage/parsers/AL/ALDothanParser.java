package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALDothanParser extends GroupBestParser {
  
  public ALDothanParser() {
    super(new ALDothanAParser(), new ALDothanBParser());
  }
}
