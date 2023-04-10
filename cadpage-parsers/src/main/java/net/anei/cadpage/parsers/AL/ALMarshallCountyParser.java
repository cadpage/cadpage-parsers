package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.GroupBestParser;


public class ALMarshallCountyParser extends GroupBestParser {

  public ALMarshallCountyParser() {
    super(new ALMarshallCountyDParser(),
          new ALMarshallCountyEParser(),
          new ALMarshallCountyFParser());
  }
}
