package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Galax, VA
 */
public class VAGalaxParser extends GroupBestParser {
  
  public VAGalaxParser() {
    super(new VAGalaxAParser(), new VAGalaxBParser());
  }
}
