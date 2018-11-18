package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class OHWayneCountyEParser extends MsgParser {
  
  public OHWayneCountyEParser() {
    super("WAYNE COUNTY", "OH");
    setFieldList("CALL ADDR APT CITY ST DATE TIME INFO");
  }
  
  @Override
  public String getFilter() {
    return "noreply@zuercherportal.com";
  }
  
  private static final Pattern DATE_TIME_MARK_PTN = Pattern.compile(" +(?:(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) - +|None\\b *)");
  private static final Pattern DATE_TIME_PTN = Pattern.compile(" +\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d +");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2}) +(\\d{5})");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    data.strCall = subject;
  
    String addr;
    Matcher match = DATE_TIME_MARK_PTN.matcher(body);
    if (!match.find()) return false;
    addr = body.substring(0, match.start());
    String date = match.group(1);
    if (date != null) {
      data.strDate = date;
      data.strTime = match.group(2);
    }
    data.strSupp = DATE_TIME_PTN.matcher(body.substring(match.end())).replaceAll("\n");
    
    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    String zip = null;
    match= ST_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState =  match.group(1);
      zip = match.group(2);
      city = p.getLastOptional(',');
    }
    if (city.length() == 0 && zip != null) city = zip;
    data.strCity = city;
    
    parseAddress(p.get(), data);
    return (data.strCall.length() > 0 || data.strCity.length() > 0);
  }
  

}
