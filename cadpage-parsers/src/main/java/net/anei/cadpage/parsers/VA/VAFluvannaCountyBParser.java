package net.anei.cadpage.parsers.VA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

public class VAFluvannaCountyBParser extends DispatchPrintrakParser {
  
  public VAFluvannaCountyBParser() {
    super(CITY_CODES, "FLUVANNA COUNTY", "VA");
  }
  
  @Override
  public String getFilter() {
    return "cad2@acuecc.org";
  }
  
  private static final Pattern INFO_ADDR_BRK_PTN = Pattern.compile("(.+?)[-,] +(.*)");
  private static final Pattern STRIP_JUNK_PTN = Pattern.compile("^[-,] *");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Sometimes a county name is store in the address
    String city = parseCity(data.strAddress);
    if (city != null) {
      String addr = data.strPlace;
      if (addr.equals(data.strAddress)) {
        Matcher match = INFO_ADDR_BRK_PTN.matcher(data.strSupp);
        if (match.matches()) {
          addr = match.group(1).trim();
          data.strSupp = match.group(2);
        }
      } else {
        data.strSupp = stripFieldStart(data.strSupp, addr);
        Matcher match = STRIP_JUNK_PTN.matcher(data.strSupp);
        if (match.find()) data.strSupp = data.strSupp.substring(match.end());
      }
      data.strAddress = data.strPlace = "";
      parseAddress(addr, data);
      data.strCity = city;
    }
    return true;
  }
  
  private static final Pattern CITY_PTN = Pattern.compile("([A-Z]+)(?: +\\d)?"); 
  
  private String parseCity(String city) {
    Matcher match = CITY_PTN.matcher(city);
    if (!match.matches()) return null;
    city = match.group(1);
    if (!COUNTY_SET.contains(city)) return null;
    return city + " COUNTY";
  }

  private static final Set<String> COUNTY_SET = new HashSet<String>(Arrays.asList(new String[]{
      "ALBEMARLE",
      "AUGUSTA",
      "BUCKINGHAM",
      "CUMBERLAND",
      "FLUVANNA",
      "GOOCHLAND",
      "GREENE",
      "LOUISA",
      "NELSON",
      "ORANGE",
      "ROCKINGHAM",
  }));
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AC", "ALBEMARLE COUNTY",
      "SC", "SCOTTSVILLE"
  });

}
