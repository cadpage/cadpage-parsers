package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class LALafourcheParishParser extends MsgParser {

  public final static Pattern SUBJECT_PTN = Pattern.compile("(\\d\\d-\\d+) - *(.*)");
  public final static Pattern DATE_TIME_PTN = Pattern.compile(" +(?:(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) - *|None$)");
  
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

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Extract the message ID from the Subject
    Matcher subMatch = SUBJECT_PTN.matcher(subject);
    if (!subMatch.matches())  return false;
    data.strCallId = subMatch.group(1);
    data.strCall = subMatch.group(2).trim();
    
    body = stripFieldStart(body, "Intersection of ");
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (!match.find()) return false;
    data.strDate = getOptGroup(match.group(1));
    data.strTime =  getOptGroup(match.group(2));
    Parser p = new Parser(body.substring(0,match.start()));
    String city = p.getLastOptional(',');
    if (city.equals("LA")) city = p.getLastOptional(',');
    data.strCity = city;
    parseAddress(p.get(), data);
    
    int last = match.end();
    while (match.find()) {
      parseInfo(body.substring(last, match.start()), data);
      last = match.end();
    }
    parseInfo(body.substring(last), data);
    return true;
  }
  
  private void parseInfo(String info, Data data) {
    if (data.strCall.length() == 0) {
      data.strCall = info;
    } else {
      data.strSupp = append(data.strSupp, "\n", info);
    }
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    
    // Replace "BY-PASS" with "BYPASS" (special case)
    sAddress = sAddress.replace("BY-PASS", "BYPASS");
    return sAddress;
  }
}
