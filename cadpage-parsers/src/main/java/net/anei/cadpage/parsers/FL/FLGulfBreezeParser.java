package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLGulfBreezeParser extends GroupBestParser {
  
  public FLGulfBreezeParser() {
    super(new FLGulfBreezeAParser(), new FLGulfBreezeBParser());
  }
} 
