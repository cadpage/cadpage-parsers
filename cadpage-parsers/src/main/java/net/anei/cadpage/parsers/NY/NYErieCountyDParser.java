package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class NYErieCountyDParser extends MsgParser {

  private static final Pattern CALL_PTN = Pattern.compile("[A-Z]{3}");
  private static final Pattern APT_PTN = Pattern.compile("^(?:APT|ROOM|RM)\\.? *([^ /]+) */? *", Pattern.CASE_INSENSITIVE);
  
  public NYErieCountyDParser() {
    super("ERIE COUNTY", "NY");
    setFieldList("CITY ADDR CODE CALL APT INFO");
  }

  @Override
  protected boolean parseMsg(String body, Data data) { 
    
    // Page must start with recognized 3 character city code
    int pt = body.indexOf(' ');
    if (pt != 3) return false;
    data.strCity = CITY_CODES.getProperty(body.substring(0,3));
    if (data.strCity == null) return false;
    body = body.substring(4).trim();
    
    // search rest of text for 3 character call code
    Matcher match = CALL_PTN.matcher(body);
    while (match.find()) {
      String code = match.group();
      String call = CALL_CODES.getProperty(code);
      if (call != null) {
        parseAddress(body.substring(0,match.start()).trim(), data);
        data.strCode = code;
        data.strCall = call;
        
        String info = body.substring(match.end()).trim();
        if (info.startsWith("*")) info = info.substring(1).trim();
        if (info.startsWith(".")) info = info.substring(1).trim();
        match = APT_PTN.matcher(info);
        if (match.find()) {
          data.strApt = match.group(1);
          info = info.substring(match.end());
        }
        data.strSupp = info;
        return true;
      }
    }
    return false;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLA", "CLARENCE",
      "AMH", "AMHERST",
      "NIA", "NIAGRA COUNTY",
      "WMV", "WILLIAMSVILLE"
  });
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "EMA", "MEDICAL",
      "EMS", "MEDICAL",
      "FAL", "FIRE ALARM",
      "MAA", "MUTUAL AID",
      "MVA", "MOTOR VEHICLE ACCIDENT",
      "MVF", "VEHICLE FIRE"
  }); 
}
	