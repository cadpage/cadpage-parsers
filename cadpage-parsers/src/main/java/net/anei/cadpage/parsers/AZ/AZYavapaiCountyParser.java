package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Yavapai County, AZ
 */
public class AZYavapaiCountyParser extends GroupBestParser {
  public AZYavapaiCountyParser() {
    super(new AZYavapaiCountyAParser(),
          new AZYavapaiCountyBParser(),
          new AZYavapaiCountyCParser(),
          new AZYavapaiCountyDParser(),
          new AZYavapaiCountyEParser());
  }
}
