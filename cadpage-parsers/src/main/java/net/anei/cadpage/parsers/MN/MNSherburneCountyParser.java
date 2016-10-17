package net.anei.cadpage.parsers.MN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;


/**
 * Sherburne County, MN
 */
public class MNSherburneCountyParser extends DispatchProphoenixParser {
  
  private static final Pattern MAP_PTN = Pattern.compile("^(\\d{4}[- /]\\d)\\b");
  
  public MNSherburneCountyParser() {
    super(CITY_CODES, "SHERBURNE COUNTY", "MN");
  }
  
  @Override
  public String getFilter() {
    return "pro.prophoenix@co.sherburne.mn.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "/ Phoenix Notification / ");
    if (!super.parseMsg(body, data)) return false;
    Matcher match = MAP_PTN.matcher(data.strSupp);
    if (match.find()) {
      data.strMap = match.group(1).replace(' ', '-').replace('.', '-');
      data.strSupp = data.strSupp.substring(match.end()).trim();
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("INFO", "MAP INFO");
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BL", "BIG LAKE", 
      "ER", "ELK RIVER"
  });
}
