package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLOrangeCountyParser extends GroupBestParser {
  
  public FLOrangeCountyParser() {
    super(new FLOrangeCountyAParser(), new FLOrangeCountyBParser());
  }
} 
