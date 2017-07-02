package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOSteGenevieveCountyParser extends GroupBestParser {

  public MOSteGenevieveCountyParser() {
    super(new MOSteGenevieveCountyAParser(), new MOSteGenevieveCountyBParser());
  }
}
