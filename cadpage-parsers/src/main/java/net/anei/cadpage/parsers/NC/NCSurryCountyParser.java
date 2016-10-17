package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Surry County, NC
 */
public class NCSurryCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("SC911::?(?:Call #|=)(\\d{6,}-\\d{4}) \\[(?:Address|Location)\\] (.*?) \\[X St\\] (.*?) \\[Type\\] (.*)");
  
  public NCSurryCountyParser() {
    super("SURRY COUNTY", "NC");
    setFieldList("ID ADDR APT CITY ST X CALL");
  }
  
  @Override
  public String getFilter() {
    return "SC911:@co.surry.nc.us,sc911@co.surry.nc.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    String addr = match.group(2).replace("//", "&").trim();
    Parser p = new Parser(addr);
    
    // State is always NC, even when location is in VA
    String state = p.getLastOptional(',');
    if (!state.equals("NC")) data.strState = state;
    
    data.strCity = p.getLastOptional("  ");
    parseAddress(p.get(), data);
    if (data.strCity.equalsIgnoreCase("CLAUDEVILLE")) data.strCity = "CLAUDVILLE";
    if (data.strCity.endsWith(" CCOUNTY")) {
      data.strCity = data.strCity.substring(0,data.strCity.length()-7) + "COUNTY";
    }
    
    if (data.strState.length() == 0 && VA_CITY_LIST.contains(data.strCity)) {
      data.strState = "VA";
    }
    
    data.strCross = match.group(3).trim().replace(" TO ", " & ");
    if (data.strCross.equals("TO")) data.strCross = "";
    else if (data.strCross.endsWith(" TO")) {
      data.strCross = data.strCross.substring(0,data.strCross.length()-3).trim();
    }
    data.strCall = match.group(4).trim();
    return true;
  }
  
  private static final Set<String> VA_CITY_LIST = new HashSet<String>(Arrays.asList(new String[]{
      "PATRICK COUNTY",
      "CLAUDVILLE"
  }));
}
