package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;

public class MDAnneArundelCountyParser extends GroupBestParser {

  public MDAnneArundelCountyParser() {
    super(new MDAnneArundelCountyFireParser(),
          new MDAnneArundelCountyEMSParser(),
          new MDAnneArundelCountyEMS2Parser(),
          new MDAnneArundelCountyFireblitzParser(),
          new MDAnneArundelCountyADSiCADParser(),
          new MDAnneArundelCountyGambrillsParser());
  }

}
