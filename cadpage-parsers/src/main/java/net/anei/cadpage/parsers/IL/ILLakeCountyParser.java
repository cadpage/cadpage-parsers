package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILLakeCountyParser extends GroupBestParser {

  public ILLakeCountyParser() {
    super(new ILLakeCountyAParser(),
          new ILLakeCountyCParser(),
          new ILLakeCountyDParser(),
          new ILLakeCountyEParser(),
          new ILLakeCountyFParser(),
          new ILLakeCountyGParser(),
          new ILLakeCountyHParser());

  }
}
