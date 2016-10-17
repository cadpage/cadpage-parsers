package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;


public class MOFestusParser extends GroupBestParser {

  public MOFestusParser() {
    super(new MOFestusAParser(), new MOFestusBParser());
  }
}
