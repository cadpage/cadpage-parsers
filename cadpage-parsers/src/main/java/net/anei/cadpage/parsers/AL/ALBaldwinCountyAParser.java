
package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Baldwin County, AL
 */
public class ALBaldwinCountyAParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("(?:- E911 Notification - ([^-]+) - )?(\\d\\d-\\d\\d-\\d\\d) (\\d\\d:\\d\\d): Station ([A-Z0-9]+) dispatched to Incident ID (\\S+) \\((.*?)\\) at (.*?)(?:\\(.*\\))?");
  private static final DateFormat DATE_FMT = new SimpleDateFormat("MM-dd-yy");
  private static final Pattern DOUBLE_NUMBER_PTN = Pattern.compile("(\\d+) +(\\d+)");

  public ALBaldwinCountyAParser() {
    super("BALDWIN COUNTY", "AL");
    setFieldList("SRC DATE TIME UNIT ID CALL ADDR APT");
  }
    
  @Override
  public String getFilter() {
    return "noreply@emergencycallworx.com,cad@baldwin911.org,messaging@iamresponding.com";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = DAPHMONT_DR_EXT.matcher(address).replaceAll("$1 EXD");
    address = YUPON_RD_EXT.matcher(address).replaceAll("$1 EXD");
    return super.adjustMapAddress(address);
  }
  private static final Pattern DAPHMONT_DR_EXT = Pattern.compile("\\b(DAPHMONT DR) EXT?\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern YUPON_RD_EXT = Pattern.compile("\\b(YUPON RD) EXT?\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Fix IAR edits
    if (subject.equals("Foley Sta. 30")) {
      String[] lines =  body.replace("\n(LSA \n(", "\n(LSA (").split(" *\n *");
      if (lines.length != 4) return false;
      body = "- E911 Notification - " + subject + " - " + lines[0] + " dispatched to " + lines[1] + " " + lines[2] + " at " + lines[3];
    } 
    else if (subject.length() > 0 && !body.startsWith("- ")) {
      body = "- " + subject + " - " + body;
    }
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = getOptGroup(match.group(1));
    setDate(DATE_FMT, match.group(2), data);
    data.strTime = match.group(3);
    data.strUnit = match.group(4);
    data.strCallId = match.group(5);
    data.strCall = match.group(6).trim();
    
    String sAddr = match.group(7).trim().replace('@', '&');
    match = DOUBLE_NUMBER_PTN.matcher(sAddr);
    if (match.matches()) sAddr = match.replaceFirst("$1 RT $2");
    parseAddress(sAddr, data);
    
    return true;
  }
}
