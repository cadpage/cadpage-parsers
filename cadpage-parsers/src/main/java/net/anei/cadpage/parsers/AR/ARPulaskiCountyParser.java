package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Pulaski County, AR
 */
public class ARPulaskiCountyParser extends GroupBestParser {
  public ARPulaskiCountyParser() {
    super(new ARPulaskiCountyAParser(),
          new ARPulaskiCountyBParser(),
          new ARPulaskiCountyCParser(),
          new ARPulaskiCountyDParser());
  }
}
