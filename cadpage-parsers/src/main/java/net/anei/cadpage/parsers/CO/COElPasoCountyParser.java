package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * El Paso County, CO
 */
public class COElPasoCountyParser extends GroupBestParser {
  

  public COElPasoCountyParser() {
    super(new COElPasoCountyAParser(),
        new COElPasoCountyBParser(),
        new COElPasoCountyCParser());
   }
}
  





