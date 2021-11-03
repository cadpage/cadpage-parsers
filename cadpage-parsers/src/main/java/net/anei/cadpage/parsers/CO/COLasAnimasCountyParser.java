package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Las Animas County, CO
 */
public class COLasAnimasCountyParser extends GroupBestParser {


  public COLasAnimasCountyParser() {
    super(new COLasAnimasCountyAParser(), new COLasAnimasCountyBParser());
   }
}






