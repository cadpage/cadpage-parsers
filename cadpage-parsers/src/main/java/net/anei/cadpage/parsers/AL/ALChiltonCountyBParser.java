package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALChiltonCountyBParser extends DispatchA71Parser {

  public ALChiltonCountyBParser() {
    super("CHILTON COUNTY", "AL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern CITY_TRAIL_PTN = Pattern.compile("(.*?) \\d+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    Matcher match = CITY_TRAIL_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    return true;
  }

}
