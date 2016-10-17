package net.anei.cadpage.parsers.PA;



public class PAChesterCountyGParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyGParser() {
    super(null);
  }
  
  @Override
  public String getFilter() {
    return "wgfc22@comcast.net,EWFC05@verizon.net,pfdfire@fdcms.info,vfvfco168@comcast.net,dispatch@station87.com";
  }
} 
