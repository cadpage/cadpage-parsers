package net.anei.cadpage.parsers.MO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOStLouisCountyHParser extends DispatchA33Parser {
  
  
  public MOStLouisCountyHParser() {
    super("ST LOUIS COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "RECORDS@BRIDGETONMO.COM";
  }
  
  private static final Pattern CITY_ST_PTN = Pattern.compile("([ A-Za-z]+), *([A-Z]{2})");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Cross street field is used as a city overflow
    if (data.strCross.equals("MO") || data.strCross.equals("IL")) {
      data.strState = data.strCross;
      data.strCross = "";
    } else {
      Matcher match = CITY_ST_PTN.matcher(data.strCross);
      if (match.matches()) {
        data.strCity = match.group(1).trim();
        data.strState = match.group(2);
        data.strCross = "";
      } else if (CITY_SET.contains(data.strCross.toUpperCase())) {
        data.strCity = data.strCross;
        data.strCross = "";
      }
    }
    return true;
  }
  
  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(MOStLouisCountyParser.CITY_LIST));
}