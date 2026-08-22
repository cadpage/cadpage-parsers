package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLPalmBeachCountyParser extends GroupBestParser {

  public FLPalmBeachCountyParser() {
    super(new FLPalmBeachCountyAParser(), new FLPalmBeachCountyBParser());
  }
}
