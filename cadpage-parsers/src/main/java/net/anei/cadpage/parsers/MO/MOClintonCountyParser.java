package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOClintonCountyParser extends GroupBestParser {

  public MOClintonCountyParser() {
    super(new MOClintonCountyAParser(),
          new MOClintonCountyBParser(),
          new MOClintonCountyCParser());
  }
}
