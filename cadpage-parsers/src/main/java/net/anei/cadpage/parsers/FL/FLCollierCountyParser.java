package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLCollierCountyParser extends GroupBestParser {
  
  public FLCollierCountyParser() {
    super(new FLCollierCountyAParser(), new FLCollierCountyBParser());
  }
} 
