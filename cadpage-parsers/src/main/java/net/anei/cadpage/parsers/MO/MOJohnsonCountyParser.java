package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOJohnsonCountyParser extends GroupBestParser {

  public MOJohnsonCountyParser() {
    super(new MOJohnsonCountyAParser(), new MOJohnsonCountyBParser());
  }
}
