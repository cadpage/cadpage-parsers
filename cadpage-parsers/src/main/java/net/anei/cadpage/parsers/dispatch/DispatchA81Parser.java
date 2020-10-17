package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class DispatchA81Parser extends MsgParser {
  
  public DispatchA81Parser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("CALL ID DATE TIME ADDR APT CITY ST INFO");
  }
  
  private static final Pattern MASTER = Pattern.compile("([A-Z]{3}\\d{10}) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b *(.*)");
  private static final Pattern INFO_MARK_PTN = Pattern.compile(";? \\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - ");
  private static final Pattern TIMES_MARK_PTN = Pattern.compile(" [-/A-Z0-9]+ - (?:Assign|Enroute|On Scene|Available) \\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d\\b");
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: (\\d{5}))");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    data.strCall = subject.length() > 0 ? subject : "ALERT";
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    body = match.group(4);
    
    body = stripFieldEnd(body, "{incident_code_description");
    
    match = INFO_MARK_PTN.matcher(body);
    if (match.find()) {
      int pt = match.start();
      int spt = match.end();
      while (match.find()) {
        data.strSupp = append(data.strSupp, "\n", body.substring(spt, match.start()).trim());
        spt = match.end();
      }
      data.strSupp = append(data.strSupp, "\n", body.substring(spt).trim());
      body = body.substring(0,pt).trim();
    }
    
    match = TIMES_MARK_PTN.matcher(body);
    if (match.find()) {
      String times = body.substring(match.start()).trim().replace("; ", "\n");
      data.strSupp = append(times, "\n", data.strSupp);
      body = body.substring(0, match.start()).trim();
    }

    Parser p = new Parser(body);
    String city = p.getLastOptional(',');
    match = STATE_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      data.strCity = match.group(2);
      city = p.getLastOptional(',');
    }
    data.strCity = city;
    parseAddress(p.get(), data);
    return true;
  }
}
