package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Buckingham County, VA
 */
public class VABuckinghamCountyParser extends GroupBestParser {

  public VABuckinghamCountyParser() {
    super(new VABuckinghamCountyAParser(), new VABuckinghamCountyBParser());
  }
}
