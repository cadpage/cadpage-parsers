package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Murray County, GA
 */
public class GAMurrayCountyParser extends GroupBestParser {

  public GAMurrayCountyParser() {
    super(new GAMurrayCountyAParser(), new GAMurrayCountyBParser());
  }
}
