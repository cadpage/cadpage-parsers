package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Navajo County, AZ
 */
public class AZNavajoCountyParser extends GroupBestParser {
  public AZNavajoCountyParser() {
    super(new AZNavajoCountyAParser(), new AZNavajoCountyBParser(), new AZNavajoCountyCParser());
  }
}
