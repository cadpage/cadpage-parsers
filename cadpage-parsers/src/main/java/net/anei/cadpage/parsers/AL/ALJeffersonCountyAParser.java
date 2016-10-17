
package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Jefferson County, AL
 */
public class ALJeffersonCountyAParser extends SmartAddressParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("^(?:RUN # )?(\\d{2}-\\d*)\n");
  private static final Pattern RUN_NO_PTN = Pattern.compile("\\d{2}-\\d+");
  private static final Pattern DELIM_PTN = Pattern.compile(" - |  +|\\.+ +|\\.\\.+");
  private static final Pattern UPDATE_PTN = Pattern.compile("^(?:UPDATE|CORRR?ECTION)");
  private static final Pattern APT_PTN = Pattern.compile("(.*?) *\\bAPT +([A-Z0-9]+)\\b *(.*?)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLEAN_END_PTN = Pattern.compile("[- \\.]*(.*?)[- \\.]*");

  public ALJeffersonCountyAParser() {
    super("JEFFERSON COUNTY", "AL");
    setFieldList("ADDR APT INFO CALL");
  }
    
  @Override
  public String getFilter() {
    return "Fire Desk,firedesk@northstar-firedesk.com,jeffcoal911@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // First check for a couple varieties of run reports
    if (RUN_NO_PTN.matcher(subject).matches()) data.strCallId = subject;
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.find()) {
      if (data.strCallId.length() == 0) data.strCallId = match.group(1);
      body = body.substring(match.end()).trim();
    }
    if (data.strCallId.length() > 0) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    
    // Regular call parsing is very hit or miss.  We only do it if we have a positive ID
    if (!isPositiveId()) return false;
    
    // But we can attempt to rule out the alerts for the other Jefferson County parsers
    if (subject.length() > 0) return false;
    
    // Get rid of any newlines
    body = body.replace('\n', ' ');
    
    // look for an update/correction indicator
    match = UPDATE_PTN.matcher(body);
    if (match.find()) {
      data.strCall = match.group();
      body = body.substring(match.end()).trim();
      if (body.startsWith("-")) body = body.substring(1).trim();
    }

    //  Start by looking for an obvious delimiter
    String addr = "";
    while (addr.length() == 0) {
      match = DELIM_PTN.matcher(body);
      if (!match.find()) break;
      addr = body.substring(0,match.start()).trim();
      body = body.substring(match.end()).trim();
    }
    
    // If we found one, what is in front of it it is an address & info
    // behind it is the call description
    if (addr.length() > 0) {
      parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT, addr.replace('@','&'), data);
      data.strSupp = cleanApt(getLeft(), data);
      data.strCall = append(data.strCall, " - ", cleanApt(body, data));
    }
    
    // If we did not find one, separate text into address and call description
    else {
      parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT, body.replace('@','&'), data);
      data.strCall = cleanApt(getLeft(), data);
      if (data.strCall.length() == 0) {
        data.strCall = "GENERAL REPORT";
        data.strPlace = body;
        data.strAddress = data.strApt = "";
      }
    }
    return true;
  }
  
  private String cleanApt(String field, Data data) {
    if (field.startsWith("/")) field = field.substring(1).trim();
    Matcher match = APT_PTN.matcher(field);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", match.group(2));
      field = append(match.group(1), " ", match.group(3));
      match = CLEAN_END_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
    }
    return field;
  }
}
