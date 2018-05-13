package net.anei.cadpage.parsers.WI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class WIRockCountyParser extends DispatchH03Parser {

  public WIRockCountyParser() {
    super(CITY_CODES, "ROCK COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "RCCC@co.rock.wi.us";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]{2,4})-Active911");

  @Override
  public boolean parseHtmlMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static final Pattern CTY_TK_PTN = Pattern.compile("\\bCTY TK\\b", Pattern.CASE_INSENSITIVE);
  @Override
  public String adjustMapAddress(String addr) {
    addr = CTY_TK_PTN.matcher(addr).replaceAll("COUNTY ROAD");
    return super.adjustMapAddress(addr);
    
  }
  
  @Override
  public String adjustMapCity(String city) {
    // Rock town gets confused with Rock county if we do not qualify it
    if (city.equals("ROCK")) city = "ROCK,ROCK COUNTY";
    return city;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BT", "BELOIT",
      "JT", "JOHNSTOWN",
      "JV", "JANESVILLE",
      "HT", "HARMONY",
      "RT", "ROCK"
  });
}
