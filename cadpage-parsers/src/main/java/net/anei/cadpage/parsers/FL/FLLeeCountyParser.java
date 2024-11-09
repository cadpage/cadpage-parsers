package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLLeeCountyParser extends GroupBestParser {

  public FLLeeCountyParser() {
    super(new FLLeeCountyAParser(),
          new FLLeeCountyBParser(),
          new FLLeeCountyCParser(),
          new FLLeeCountyDParser());
  }
}
