package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class NYSuffolkCountyEParser extends SmartAddressParser {

  private static final Pattern MARKER = Pattern.compile("^(?:/[A-Z ]*RELAY */|(?:FROM )?RELAY )");
  private static final Pattern DELIM = Pattern.compile(" *[\n;] *|  +| *(?<!XS)[,:]+ *");
  private static final Pattern OFF_OF = Pattern.compile("\\bOFF OF\\b");
  
  public NYSuffolkCountyEParser() {
    super(CITY_LIST, "SUFFOLK COUNTY","NY");
    setFieldList("PLACE ADDR APT CITY INFO");
    removeWords("STE");
  }

  @Override
  public String getFilter() {
    return "Bob@relaycom.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    do {
      Matcher match = MARKER.matcher(body);
      if (match.find()) {
        body = body.substring(match.end()).trim();
        break;
      }
      
      if (subject.contains("FROM RELAY")) break;
      
      return false;
    } while (false);
    
    // Punctuation is too unreliable to count on
    body = body.replace(".", "");
    body = OFF_OF.matcher(body).replaceAll("XS:");
    
    String[] flds = DELIM.split(body);
    if (flds.length == 1) {
      
      // Feed everything to smart parser
      parseAddress(StartType.START_PLACE, FLAG_IGNORE_AT | FLAG_NO_IMPLIED_APT, body, data);
      data.strSupp = getLeft();
      
      // Clean up loose ends
      String sAddr = data.strAddress;
      if (sAddr.endsWith(" IN")) data.strAddress = sAddr.substring(0, sAddr.length()-3).trim();
      
      if (data.strPlace.length() > 0 && data.strAddress.length() == 0 && data.strSupp.length() == 0) {
        data.strSupp = data.strPlace;
        data.strPlace = "";
      }
    } 
    
    else {
      int pt = 0;
      Result res = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END | FLAG_CHECK_STATUS | FLAG_NO_IMPLIED_APT, flds[0]);
      if (flds.length > 2) {
        Result res2 = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END | FLAG_CHECK_STATUS | FLAG_NO_IMPLIED_APT, flds[1]);
        if (res2.getStatus() > res.getStatus()) {
          data.strPlace = flds[0];
          res = res2;
          pt = 1;
        } 
      }
      res.getData(data);
      
      for (pt++; pt < flds.length; pt++) {
        data.strSupp = append(data.strSupp, " - ", flds[pt]);
      }
    }
    
    if (data.strAddress.endsWith(" IN")) {
      data.strAddress = data.strAddress.substring(0,data.strAddress.length()-3).trim();
    }
    
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
      "AQUEBOGUE",
      "JAMESPORT"
  };
}
