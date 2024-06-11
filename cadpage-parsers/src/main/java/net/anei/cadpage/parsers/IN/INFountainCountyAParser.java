package net.anei.cadpage.parsers.IN;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

/**
 * Fountain County, IN
 */
public class INFountainCountyAParser extends DispatchA29Parser {

  public INFountainCountyAParser() {
    super(INFountainCountyParser.CITY_LIST, "FOUNTAIN COUNTY", "IN");
    setupCallList(CALL_LIST);
  }

  @Override
  public String getFilter() {
    return "@fwrdc.net";
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT - UNKNOWN",
      "AMBULANCE EMERGENCY",
      "FIRE - ALARM",
      "FIRE - GRASS/BRUSH",
      "FIRE - STRUCTURE"
  );

  private static final Pattern DIR_ABBRV_PTN = Pattern.compile("\\b(?:NO|SO|EA|WE)\\b");

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = DIR_ABBRV_PTN.matcher(data.strAddress);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        match.appendReplacement(sb, match.group().substring(0, 1));
      } while (match.find());
      match.appendTail(sb);
      data.strAddress = sb.toString();
    }
    return true;
  }
}
