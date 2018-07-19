package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class TXLibertyCountyBParser extends MsgParser {
  
  public TXLibertyCountyBParser() {
    super("LIBERTY COUNTY", "TX");
    setFieldList("DATE TIME ID CODE CALL ADDR APT CITY X SRC UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@co.liberty.tx.us";
  }
  
  private static final Pattern MASTER1 = Pattern.compile("DISPATCH:\\S+:#\\S+ - (\\d\\d/\\d\\d) (\\d\\d:\\d\\d) - \\S+:(\\d{2}-\\d{6}) (\\d{4}) (.*)\n{2,}(.*?)//(?:(.*?)//)?(\\S+):#(\\S+)//(.*)");
  private static final Pattern MASTER2 = Pattern.compile("DISPATCH:(\\S+):#(\\S+) - (\\d{4}) ([^/]*?)/(.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf(" / DISPATCH:");
    if (pt >= 0) body = body.substring(pt+3);
    
    // see if this is the more common 2 line format
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCallId = match.group(3);
      data.strCode = match.group(4);
      data.strCall = match.group(5);
      parseAddr(match.group(6), data);
      data.strCross = getOptGroup(match.group(7));
      data.strSource = match.group(8);
      data.strUnit = match.group(9);
      data.strSupp = match.group(10).trim();
      return true;
    }
    
    // Else try the single line format
    match = MASTER2.matcher(body);
    if (match.matches()) {
      data.strSource = match.group(1);
      data.strUnit = match.group(2);
      data.strCode = match.group(3);
      data.strCall = match.group(4);
      parseAddr(match.group(5), data);
      return true;
    }
    return false;
  }
  
  private static final Pattern CITY_PTN = Pattern.compile("[A-Z]+(?: [A-Z]+)?");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  
  private void parseAddr(String field, Data data) {
    field = field.trim();
    int pt = field.lastIndexOf(',');
    String city = field.substring(pt+1).trim();
    if (CITY_PTN.matcher(city).matches()) {
      field = field.substring(0,pt).trim();
      data.strCity = city;
    }
    field = field.replace(',', ' ');
    field = MBLANK_PTN.matcher(field).replaceAll(" ");
    parseAddress(field, data);
  }
}
