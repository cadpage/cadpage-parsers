package net.anei.cadpage.parsers.GA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Dade County, GA
 */
public class GADadeCountyParser extends SmartAddressParser {
  
  private static final Pattern MULT_SPACE_PTN = Pattern.compile("  +");
  
  public GADadeCountyParser() {
    super(CITY_LIST, "DADE COUNTY", "GA");
    setFieldList("CALL ADDR APT PLACE CITY X NAME INFO");
  }
  
  @Override
  public String getFilter() {
    return "E911@dadega.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("!")) return false;
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_CROSS_FOLLOWS | FLAG_IGNORE_AT, body, data);
    if (data.strAddress.length() == 0) return false;
    if (data.strCity.length() > 0) {
      String city = PLACE_CITY_XREF.getProperty(data.strCity);
      if (city != null) {
        data.strPlace = data.strCity;
        data.strCity = city;
      }
    }
    body = getLeft();

    String info = "";
    if (isMBlankLeft()) {
      info = body;
      body = "";
    } else {
      Matcher match = MULT_SPACE_PTN.matcher(body);
      if (match.find()) {
        info = body.substring(match.end());
        body = body.substring(0,match.start());
      }
    }
    
    if (body.length() > 0) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, body, data);
      data.strName = cleanWirelessCarrier(getLeft());
    }
    
    if (info.length() > 0) {
      info = MULT_SPACE_PTN.matcher(info).replaceAll("\n");
      int pt = info.indexOf("\nE911 Info");
      if (pt >= 0) info = info.substring(0,pt);
      data.strSupp = info;
    }

    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "NEW ENGLAND",
    "NEW SALEM",
    "RISING FAWN",
    "TRENTON",
    "WEST BROW",
    "WILDWOOD",
    
    "SLYGO"
  };
  
  private static final Properties PLACE_CITY_XREF = buildCodeTable(new String[]{
      "NEW SALEM",   "",
      "SLYGO",       "TRENTON",
      "WEST BROW",   ""
  }); 
}
