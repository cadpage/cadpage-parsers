package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class LALafourcheParishParser extends SmartAddressParser {

  public LALafourcheParishParser() {
    super("LAFOURCHE PARISH", "LA");
    setFieldList("ID CALL ADDR APT CITY DATE TIME INFO");
  }
  
  @Override
  public String getFilter() {
    return "ledsportal@lpso.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  public final static Pattern SUBJECT_PTN = Pattern.compile("(\\d\\d-\\d+) - *(.*)");
  private static final Pattern LA_ZIP_PTN = Pattern.compile(", *LA(?: (\\d{5}))?\\b");
  public final static Pattern DATE_TIME_PTN = Pattern.compile("(?: +|^)(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) - *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Extract the message ID from the Subject
    Matcher subMatch = SUBJECT_PTN.matcher(subject);
    if (!subMatch.matches())  return false;
    data.strCallId = subMatch.group(1);
    data.strCall = subMatch.group(2).trim();
    
    body = stripFieldStart(body, "Intersection of ");
    body = stripFieldEnd(body, " None");
    Matcher match = LA_ZIP_PTN.matcher(body);
    if (match.find()) {
      String zip = match.group(1);
      String addr = body.substring(0,match.start()).trim();
      String info =  body.substring(match.end()).trim();
      
      int pt = addr.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = addr.substring(pt+1).trim();
        addr = addr.substring(0, pt).trim();
      }
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
      parseAddress(addr, data);
      
      match = DATE_TIME_PTN.matcher(info);
      int last = 0;
      while (match.find()) {
        data.strDate = getOptGroup(match.group(1));
        data.strTime =  getOptGroup(match.group(2));
        parseInfo(info.substring(last, match.start()).trim(), data);
        last = match.end();
      }
      parseInfo(info.substring(last), data);
      return true;
    }
    
    else {
      match = DATE_TIME_PTN.matcher(body);
      int last = 0;
      while (match.find()) {
        String part = body.substring(last, match.start()).trim();
        data.strDate = getOptGroup(match.group(1));
        data.strTime =  getOptGroup(match.group(2));
        if (last == 0) {
          parseAddress(StartType.START_ADDR, part, data);
          part = getLeft();
        }
        parseInfo(part, data);
        last = match.end();
      }
      String part = body.substring(last).trim();
      if (last == 0) {
        parseAddress(StartType.START_ADDR, part, data);
        part = getLeft();
      }
      
      parseInfo(body.substring(last), data);
      return true;
    }
  }
  
  private void parseInfo(String info, Data data) {
    for (String part : info.split(";")) {
      part = part.trim();
      part = stripFieldEnd(part, ";");
      if (data.strCall.length() == 0) {
        data.strCall = part;
      } else {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    
    // Replace "BY-PASS" with "BYPASS" (special case)
    sAddress = sAddress.replace("BY-PASS", "BYPASS");
    return sAddress;
  }
}
