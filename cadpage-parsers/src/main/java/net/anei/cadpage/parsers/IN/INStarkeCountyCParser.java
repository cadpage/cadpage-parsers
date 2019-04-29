package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class INStarkeCountyCParser extends DispatchA48Parser {
  
  public INStarkeCountyCParser() {
    super(INStarkeCountyParser.CITY_LIST, "STARKE COUNTY", "IN", FieldType.NONE, A48_OPT_CODE);
  }

  private static final Pattern BAD_TIME_PTN = Pattern.compile("\\b(\\d\\d?:\\d\\d)(\\d\\d)\\b");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = BAD_TIME_PTN.matcher(body).replaceAll("$1:$2");
    if (!body.startsWith(":")) body = ":" + body;
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = convertCodes(data.strCity.toUpperCase(), INStarkeCountyParser.CITY_FIXES);
    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    return INStarkeCountyParser.baseAdjustMapAddress(addr);
  }
}
