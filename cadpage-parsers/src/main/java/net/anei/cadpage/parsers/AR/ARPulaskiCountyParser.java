package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Pulaski County, AR
 */
public class ARPulaskiCountyParser extends GroupBestParser {
  public ARPulaskiCountyParser() {
    super(new ARPulaskiCountyCParser(),
          new ARPulaskiCountyDParser(),
          new ARPulaskiCountyEParser());
  }
}
