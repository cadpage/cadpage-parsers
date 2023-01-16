package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCDareCountyParser extends GroupBestParser {

  public NCDareCountyParser() {
    super(new NCDareCountyAParser(), new NCDareCountyBParser());
  }
}
