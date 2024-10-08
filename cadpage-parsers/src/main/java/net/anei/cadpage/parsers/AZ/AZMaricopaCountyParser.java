package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Maricopa County, AZ
 */
public class AZMaricopaCountyParser extends GroupBestParser {
  public AZMaricopaCountyParser() {
    super(new AZMaricopaCountyAParser(),
          new AZMaricopaCountyDParser(),
          new AZMaricopaCountyEParser());
  }
}
