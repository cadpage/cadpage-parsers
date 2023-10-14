package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Benton County, AR
 */
public class ARBentonCountyParser extends GroupBestParser {
  public ARBentonCountyParser() {
    super(new ARBentonCountyAParser(), new ARBentonCountyBParser(),
          new ARBentonCountyCParser(), new ARBentonCountyDParser(),
          new ARBentonCountyEParser(), new ARBentonCountyFParser(),
          new ARBentonCountyIParser());
  }
}
