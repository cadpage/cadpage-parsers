package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Dougherty County, GA
 */
public class GADoughertyCountyParser extends GroupBestParser {

  public GADoughertyCountyParser() {
    super(new GADoughertyCountyAParser(), new GADoughertyCountyBParser());
  }
}
