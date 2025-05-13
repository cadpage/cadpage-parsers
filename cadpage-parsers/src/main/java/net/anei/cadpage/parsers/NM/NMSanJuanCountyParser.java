package net.anei.cadpage.parsers.NM;

import net.anei.cadpage.parsers.GroupBestParser;


/*
San Juan County, NM

*/

public class NMSanJuanCountyParser extends GroupBestParser {

  public NMSanJuanCountyParser() {
    super(new NMSanJuanCountyAParser(),
          new NMSanJuanCountyBParser());
  }
}
