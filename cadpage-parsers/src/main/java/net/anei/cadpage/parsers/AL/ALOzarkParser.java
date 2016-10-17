package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Ozark, AL
 */
public class ALOzarkParser extends SmartAddressParser {
  
  private static final Pattern DELIM = Pattern.compile("[ ,](OFF|REF) ");
  private static final Pattern CALL_DELIM = Pattern.compile("[\\.,\"]");
  
  public ALOzarkParser() {
    super("OZARK", "AL");
    setFieldList("CALL ADDR APT X INFO NAME");
  }
  
  @Override
  public String getFilter() {
    return "@ozarkdale911.org,ozarkdale911@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean confirm = subject.equals("ALERT!");
    
    int pt = body.lastIndexOf('\n');
    if (pt >= 0) {
      data.strName = body.substring(pt+1).trim();
      body = body.substring(0,pt);
    }
    Matcher match = DELIM.matcher(body);
    String lastDelim = "";
    int lastPt = 0;
    String sAddr = "";
    while (match.find()) {
      String field = body.substring(lastPt, match.start()).trim();
      if (lastPt == 0) sAddr = field;
      else data.strCross = append(data.strCross, " & ", field);
      lastPt = match.end();
      lastDelim = match.group(1);
      if (lastDelim.equals("REF")) break;
    }
    
    // If there was no REF key, use the first comma instead
    // Not acceptable if we haven't confirmed this is a page with the proper subject
    if (! lastDelim.equals("REF")) {
      if (!confirm) return false;
      pt = body.indexOf(',', lastPt);
      if (pt >= 0) {
        lastDelim = "REF";
        String field = body.substring(lastPt, pt).trim();
        if (lastPt == 0) sAddr = field;
        else data.strCross = append(data.strCross, " & ", field);
        lastPt = pt+1;
      }
    }
    
    // deal with last segment of text
    String field = body.substring(lastPt).trim();
    if (lastPt == 0) sAddr = field;
    else if (lastDelim.equals("REF")) data.strSupp = field;
    else data.strCross = append(data.strCross, " & ", field);

    // Next we have to figure out a how to separate the call from the address
    // It might be enclosed in double quotes, or marked by a period or comma
    boolean quote = sAddr.startsWith("\"");
    if (quote) sAddr = sAddr.substring(1);
    StartType sType = StartType.START_CALL;
    match =  CALL_DELIM.matcher(sAddr);
    if (match.find()) {
      sType = StartType.START_ADDR;
      data.strCall = sAddr.substring(0, match.start()).trim();
      sAddr = sAddr.substring(match.end()).trim();
    }
    
    // We may have already found a call description at the front of the address
    // We may already have identified the end of the the address and not need
    // to extract a long description from the end of it
    // But it all works out to some kind of call to the smart address parser
    parseAddress(sType, (lastPt == 0 ? 0 : FLAG_ANCHOR_END), sAddr, data);
    if (lastPt == 0) data.strSupp = getLeft();
    return true;
  }
}
