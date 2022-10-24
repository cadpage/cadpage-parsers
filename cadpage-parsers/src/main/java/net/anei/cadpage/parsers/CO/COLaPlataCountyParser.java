package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * La Plata County, CO
 */
public class COLaPlataCountyParser extends GroupBestParser {


  public COLaPlataCountyParser() {
    super(new COLaPlataCountyAParser(), new COLaPlataCountyBParser());
   }
}






