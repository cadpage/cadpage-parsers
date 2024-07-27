package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;

public class NCGastonCountyParser extends GroupBestParser {

  public NCGastonCountyParser() {
    super(new NCGastonCountyAParser(),
          new NCGastonCountyCParser(),
          new NCGastonCountyDParser());
  }
}
