package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCNorthamptonCountyParser extends GroupBestParser {

  public NCNorthamptonCountyParser() {
    super(new NCNorthamptonCountyAParser(), new NCNorthamptonCountyBParser(), new NCNorthamptonCountyCParser());
  }
}
