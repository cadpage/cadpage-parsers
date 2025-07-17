package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLCitrusCountyParser extends GroupBestParser {

  public FLCitrusCountyParser() {
    super(new FLCitrusCountyAParser(),
          new FLCitrusCountyBParser(),
          new FLCitrusCountyCParser());
  }
}
