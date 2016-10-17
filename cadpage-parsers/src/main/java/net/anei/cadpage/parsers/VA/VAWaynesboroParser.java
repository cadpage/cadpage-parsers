package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Waynesboro, VA
 */
public class VAWaynesboroParser extends GroupBestParser {
  
  public VAWaynesboroParser() {
    super(new VAWaynesboroAParser(), new VAWaynesboroBParser());
  }
}
