package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INMarshallCountyParser extends GroupBestParser {

  public INMarshallCountyParser() {
    super(new INMarshallCountyAParser(), new INMarshallCountyBParser());
  }

}
