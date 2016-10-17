package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCCravenCountyParser extends GroupBestParser {

  public NCCravenCountyParser() {
    super(new NCCravenCountyAParser(), new NCCravenCountyBParser(), new NCCravenCountyCParser());
  }
}
