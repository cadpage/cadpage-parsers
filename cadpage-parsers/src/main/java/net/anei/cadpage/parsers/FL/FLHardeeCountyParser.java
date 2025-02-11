package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLHardeeCountyParser extends GroupBestParser {

  public FLHardeeCountyParser() {
    super(new FLHardeeCountyAParser(), new FLHardeeCountyBParser());
  }
}
