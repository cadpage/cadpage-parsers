package net.anei.cadpage.parsers.CO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class COTellerCountyParser extends MsgParser {
  
  public COTellerCountyParser() {
    super("TELLER COUNTY", "CO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "ept@ept911.info";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    FParser p = new FParser(body);
    if (p.check("Add: ")) {
      setFieldList("ADDR APT PLACE CALL CITY");
      parseAddress(p.get(35), data);
      data.strPlace = p.get(35);
      if (!p.check("Problem: ")) return false;
      data.strCall = p.get(35);
      if (!p.check("City: ")) return false;
      data.strCity = p.get();
      return true;
    }
    
    if (p.check("Add:") || p.check("ADD:")) {
      if (p.checkAhead(80, "Prob:") || p.checkAhead(80, "PROB:")) {
        setFieldList("ADDR APT CALL");
        parseAddress(p.get(80), data);
        p.skip(5);
        data.strCall = p.get();
        return true;
      } else {
        setFieldList("ADDR CALL APT PLACE CODE");
        parseAddress(p.get(35), data);
        if (!p.check("Problem:")) return false;
        String call = p.getOptional(30, "Apt:");
        if (call != null) {
          data.strCall = call;
          data.strApt = p.get(5);
        } else {
          data.strCall = p.get(35);
        }
        if (!p.check("Loc:")) return false;
        data.strPlace = p.get(70);
        if (!p.check("Code:")) return false;
        data.strCode = p.get();
        return true;
      }
    }
    
    if (p.check("Add")) {
      setFieldList("ADDR  APT PLACE CALL CITY");
      String addr = p.getOptional(30, "Problem");
      if (addr != null) {
        parseAddress(addr, data);
        data.strCall = p.get();
        return true;
      }
      parseAddress(p.get(35), data);
      data.strPlace = p.get(35);
      if (!p.check("Problem")) return false;
      data.strCall = p.get(35);
      if (!p.check("City")) return false;
      data.strCity = p.get(35);
      return true;
    }
    return false;
  }
  
  
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "28541 N HWY 67",    "39.052499,-105.094141",
      "1364 CR 75",        "39.045712,-105.094569"
  });
}
