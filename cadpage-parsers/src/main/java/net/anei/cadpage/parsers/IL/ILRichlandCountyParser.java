package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class ILRichlandCountyParser extends SmartAddressParser {

  private static final Pattern MASTER1 = Pattern.compile("Address: +(.*?) +- +(\\d?) +Sector: +(.*?) +GEO: +(.*?) +ESZ: +(.*?) +Ward: *(.*?) *");
  private static final Pattern MASTER2 = Pattern.compile("Address: +(.*?) +--(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d(?::\\d\\d)?) ([A-Z0-9]+)-");
  private static final Pattern MASTER3 = Pattern.compile("(.*?) - (.*?) - (.*)");
  public ILRichlandCountyParser() {
    super(CITY_LIST, "RICHLAND COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (body.endsWith("-")) body += ' ';
    
    Matcher match;

    // There are two new formats
    if (subject.equals("OLNE 1600")) {
      
      String[] flds = body.split(" *\n *");
      
      match = MASTER1.matcher(flds[0]);
      if (match.matches()) {
        setFieldList("CALL ADDR APT CITY PRI MAP X");
        data.strCall = "ALERT";
        parseMyAddress(match.group(1), data);
        data.strPriority = match.group(2);
        String map = "";
        for (int jj = 3; jj<=6; jj++) {
          map = append(map, "-", match.group(jj));
        }
        data.strMap = map;
        
        if (flds.length > 1) {
          if (flds[1].startsWith("Intersection:")) {
            data.strCross = flds[1].substring(13);
          } else {
            if (!"Intersection:".startsWith(flds[1])) return false;
          }
        }
        return true;
      }
      match = MASTER2.matcher(flds[0]);
      if (match.matches()) {
        if (flds.length < 2) return false;
        setFieldList("ADDR APT CITY DATE TIME UNIT CALL");
        parseMyAddress(match.group(1), data);
        data.strDate = match.group(2);
        data.strTime = match.group(3);
        data.strUnit = match.group(4);
        data.strCall = flds[1];
        return true;
      }
      
      return false;
    }
    
    match = MASTER3.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ADDR APT CITY INFO");
      data.strSource = match.group(1).trim();
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(2).trim(), data);
      String info = match.group(3).trim();
      if (info.startsWith("-")) info = info.substring(1).trim();
      if (info.endsWith("-")) info = info.substring(0,info.length()-1).trim();
      data.strSupp = info;
      
      int len = data.strSupp.length();
      if (len == 0) {
        data.strCall = "ALERT";
      } else if (len <= 40) {
        data.strCall = data.strSupp;
        data.strSupp = "";
      }
      return true;
    }
    
    return false;
  }
  
  private void parseMyAddress(String field, Data data) {
    Parser p = new Parser(field);
    String city = p.getLastOptional(',');
    if (city.equals("IL")) city = p.getLastOptional(',');
    parseAddress(p.get(), data);
    data.strCity = city;
  }
  
  private static final String[] CITY_LIST = new String[]{
      // Incorporated
      "CALHOUN",
      "CLAREMONT",
      "NOBLE",
      "OLNEY",
      "PARKERSBURG",
      // Unincorporated
      "BERRYVILLE",
      "DUNDAS",
      "WYNOOSE",
      // Townships
      "BONPAS TWP",
      "CLAREMONT TWP",
      "DECKER TWP",
      "DENVER TWP",
      "GERMAN TWP",
      "MADISON TWP",
      "NOBLE TWP",
      "OLNEY TWP",
      "PRESTON TWP"
  };
  
}
