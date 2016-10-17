package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLCharlotteCountyParser extends GroupBestParser {
  
  public FLCharlotteCountyParser() {
    super(new FLCharlotteCountyAParser(), new FLCharlotteCountyBParser());
  }
} 
