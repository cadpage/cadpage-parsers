
package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Henry County, AL
 */
public class ALHenryCountyParser extends SmartAddressParser {

  public ALHenryCountyParser() {
    super("HENRY COUNTY", "AL");
    setFieldList("CALL ADDR CITY INFO");
    
    
  }
  @Override
  public String getFilter() {
    return "Janet.Toliver@comcast.net";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Highly promiscuous parser.  So we better make sure that the caller
    // has identified this as a dispatch page
    if (!isPositiveId()) return false;
    
    body = body.replace('@', '&');
    parseAddress(StartType.START_CALL, FLAG_IGNORE_AT, body, data);
    data.strSupp = getLeft();
    Parser p = new Parser(data.strSupp);
    if (p.get(' ').equals("IN")) {
      data.strCity = p.get(' ');
      data.strSupp = p.get();
    }
    return data.strAddress.length() > 0;
  }
}
