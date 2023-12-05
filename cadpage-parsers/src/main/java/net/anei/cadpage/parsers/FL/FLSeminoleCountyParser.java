package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLSeminoleCountyParser extends GroupBestParser {

  public FLSeminoleCountyParser() {
    super(new FLSeminoleCountyAParser(), new FLSeminoleCountyBParser());
  }
}
