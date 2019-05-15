package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class ILLakeCountyDParser extends MsgParser {
  
  public ILLakeCountyDParser() {
    this("LAKE COUNTY");
  }
  
  public ILLakeCountyDParser(String defCity) {
    super(defCity, "IL");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@redcenter.org";
  }
  
  @Override
  public String getAliasCode() {
    return "ILLakeCountyD";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("RED CENTER")) return false;
    
    FParser fp = new FParser(body);
    
    String unit = fp.getOptional(30, "YOU'RE DUE TO ");
    if (unit == null) unit = fp.getOptional(30,  "IS/ARE DUE TO "); 
    if (unit !=  null) {
      setFieldList("UNIT ADDR APT CITY CALL");
      data.strUnit = unit;
      parseAddress(fp.get(30), data);
      if (!fp.check("APT:")) return false;
      String apt = fp.getOptional("IN", 6, 7);
      if (apt == null) return false;
      data.strApt = append(data.strApt, "-", apt);
      String cityCall = fp.get();
      int pt = cityCall.indexOf("FOR THE ");
      if (pt < 0) return false;
      data.strCity = convertCodes(cityCall.substring(0, pt).trim(), CITY_CODES);
      data.strCall = cityCall.substring(pt+8).trim();
      return true;
    }
    
    unit = fp.getOptional(11, "is due to the sceen of the ");
    if (unit != null) {
      setFieldList("UNIT CALL ADDR APT");
      data.strUnit = unit;
      data.strCall = fp.get(30);
      if (!fp.check("at ")) return false;
      parseAddress(fp.get(), data);
      return true;
    }
    
    String call = fp.getOptional(48, "AT ");
    if (call != null) {
      setFieldList("CALL ADDR APT CITY");
      data.strCall = call;
      parseAddress(fp.get(30), data);
      if (!fp.check("APT:")) return false;
      String apt = fp.getOptional(", IN", 6, 7);
      if (apt == null) return false;
      data.strApt = append(data.strApt, "-", apt);
      data.strCity = convertCodes(fp.get(), CITY_CODES);
      return true;
    }
    
    return false;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "PROSPECT HEI",   "PROSPECT HEIGHTS"
  });
}
