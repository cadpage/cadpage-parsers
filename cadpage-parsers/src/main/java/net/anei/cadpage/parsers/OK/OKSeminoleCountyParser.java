package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchA35Parser;


public class OKSeminoleCountyParser extends DispatchA35Parser {
  
  public OKSeminoleCountyParser() {
    this("SEMINOLE COUNTY", "OK");
  }
  
  public OKSeminoleCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getAliasCode() {
    return "OKSeminoleCounty";
  }
  
  @Override
  public String getFilter() {
    return "cadsystem@semcoe911.com,cadsystem911@yahoo.com,jjonese911@yahoo.com,info@pottcoe911.com";
  }
}
