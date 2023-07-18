package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA87Parser;

public class MIBerrienCountyParser extends DispatchA87Parser {

  public MIBerrienCountyParser() {
    super("BERRIEN COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "Dispatch@berriencounty.org";
  }
  
  private static final Pattern CITY_CODE_PTN = Pattern.compile("\\d\\d +(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CITY_CODE_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    return true;
  }
}
