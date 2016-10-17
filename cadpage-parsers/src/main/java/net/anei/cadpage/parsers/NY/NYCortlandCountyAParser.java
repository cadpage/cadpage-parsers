package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYCortlandCountyAParser extends MsgParser {
  
  private static Pattern MARKER = Pattern.compile("^\\d\\d:\\d\\d , ");
  
  public NYCortlandCountyAParser() {
    super("CORTLAND COUNTY", "NY");
    setFieldList("ADDR CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "cville@fdcms.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() == 0) return false;
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    
    parseAddress(subject, data);
    
    body = body.substring(match.end()).trim();
    body = body.replaceAll("\\s+", " ");
    if (body.endsWith(",")) body = body.substring(0,body.length()-1).trim();
    Parser p = new Parser(body);
    data.strCall = p.get(',');
    data.strSupp = p.get();
    return true;
  }

}
