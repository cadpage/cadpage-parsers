package net.anei.cadpage.parsers.ID;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;


public class IDGoodingCountyBParser extends DispatchA22Parser {
  
  public IDGoodingCountyBParser() {
   this("GOODING COUNTY", "ID");
  }
  
  public IDGoodingCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getFilter() {
    return "PagingService@sircomm.com,Paging@sircomm.com";
  }

  @Override
  public String getAliasCode() {
    return "IDGoodingBCounty";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n------");
    body = body.replace("\n# ", "\nEVENT # ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city.toUpperCase(), MAP_CITY_TABLE);
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "GDG AREA",   "GOODING",
      "WND AREA",   "WENDELL"
  });
  
}
