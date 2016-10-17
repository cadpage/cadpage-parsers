package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * San Diego County, CA
 */
public class CASanDiegoCountyParser extends GroupBestParser {
  public CASanDiegoCountyParser() {
    super(new CASanDiegoCountyAParser(), new CASanDiegoCountyBParser(), 
          new CASanDiegoCountyCParser(), new CASanDiegoCountyDParser());
  }
}
