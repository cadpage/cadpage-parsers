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
  
  private static final Pattern MISSING_INFO = Pattern.compile("(?<=[A-Z])(?=\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d : )");
  private static final Pattern MASTER = Pattern.compile("SC911::?(?:Call #|=)(\\d{6,}-\\d{4}) \\[(?:Address|Location)\\] (.*?) \\[X St\\] (.*?) \\[Type\\] (.*)\\[Info\\] *(.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" {2,}");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} .*|Cross st.*|EMD .*");
  
  public NCSurryCountyParser() {
    super("SURRY COUNTY", "NC");
    setFieldList("ID ADDR APT CITY ST X CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "SC911:@co.surry.nc.us,sc911@co.surry.nc.us,Surry911@co.surry.nc.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Fix missing [Info] tags
    if (!body.contains("[Info]")) {
      Matcher match = MISSING_INFO.matcher(body);
      if (match.find()) {
        body =  body.substring(0,match.start()) + "[Info]" + body.substring(match.end());
      } else {
        body += "[Info]";
      }
    }
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
    
    String info = match.group(5).trim();
    for (String line : INFO_BRK_PTN.split(info)) {
      if (INFO_JUNK_PTN.matcher(line).matches()) continue;
      data.strSupp = append(data.strSupp, "\n", line);
    }
    
    return true;
  }
  
  private static final Set<String> VA_CITY_LIST = new HashSet<String>(Arrays.asList(new String[]{
      "PATRICK COUNTY",
      "CLAUDVILLE"
  }));
}
