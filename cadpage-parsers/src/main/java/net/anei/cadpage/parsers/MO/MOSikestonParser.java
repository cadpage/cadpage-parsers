package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOSikestonParser extends GroupBestParser {

  public MOSikestonParser() {
    super(new MOSikestonAParser(), new MOSikestonBParser());
  }
}
