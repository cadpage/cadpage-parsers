package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INHowardCountyParser extends GroupBestParser {

  public INHowardCountyParser() {
    super(new INHowardCountyAParser(),
          new INHowardCountyBParser(),
          new INHowardCountyCParser());
  }

}
