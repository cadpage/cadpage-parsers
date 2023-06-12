package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class COAlamosaCountyParser extends DispatchH03Parser {

  public COAlamosaCountyParser() {
    super("ALAMOSA COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "CG@csp.noreply";
  }

  private static final Pattern CITY_PFX_PTN = Pattern.compile("[-0-9]+ +(.*)");
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    Matcher match = CITY_PFX_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    return true;
  }
}
