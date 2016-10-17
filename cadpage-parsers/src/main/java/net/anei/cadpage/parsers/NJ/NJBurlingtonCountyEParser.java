package net.anei.cadpage.parsers.NJ;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class NJBurlingtonCountyEParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("(.*) VENUE : (.*) (\\d\\d:\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d{4})");
  
  public NJBurlingtonCountyEParser() {
    super("BURLINGTON COUNTY", "NJ");
    setFieldList("CALL ADDR CITY INFO DATE TIME");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    String sAddr = match.group(1).trim();
    String sExtra  = match.group(2).trim();
    data.strTime = match.group(3);
    data.strDate = match.group(4);
    
    // Address contains call and address
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, sAddr, data);
    
    // Info portion may start with a city
    Parser p = new Parser(sExtra);
    String sCity = p.get(' ');
    if (CITY_SET.contains(sCity.toUpperCase())) {
      data.strCity = sCity;
      sExtra = p.get();
    }
    data.strSupp = sExtra;
    
    return true;
  }
  
  
  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(new String[]{
      "EVESHAM"
  }));
  
}
