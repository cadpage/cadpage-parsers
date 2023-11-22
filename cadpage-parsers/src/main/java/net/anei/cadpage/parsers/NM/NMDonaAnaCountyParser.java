package net.anei.cadpage.parsers.NM;

import net.anei.cadpage.parsers.GroupBestParser;


/*
Dona Ana County, NM

*/

public class NMDonaAnaCountyParser extends GroupBestParser {

  public NMDonaAnaCountyParser() {
    super(new NMDonaAnaCountyAParser(),
          new NMDonaAnaCountyBParser(),
          new NMDonaAnaCountyDParser());
  }
}
