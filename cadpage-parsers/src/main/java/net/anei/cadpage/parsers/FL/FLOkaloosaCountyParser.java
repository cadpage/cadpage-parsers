package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;

public class FLOkaloosaCountyParser extends GroupBestParser {

  public FLOkaloosaCountyParser() {
    super(new FLOkaloosaCountyAParser(),
          new FLOkaloosaCountyBParser(),
          new FLOkaloosaCountyCParser());
  }
}
