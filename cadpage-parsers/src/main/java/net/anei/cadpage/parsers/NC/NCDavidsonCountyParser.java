package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCDavidsonCountyParser extends GroupBestParser {

  public NCDavidsonCountyParser() {
    super(new NCDavidsonCountyAParser(), new NCDavidsonCountyBParser());
  }
}
