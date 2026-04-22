package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WAGraysHarborCountyBParser extends DispatchA19Parser {

  public WAGraysHarborCountyBParser() {
    super("GRAYS HARBOR COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "noreply@gh911.org";
  }

  private static final Pattern APT_PLACE_PTN = Pattern.compile("(\\S*\\d\\S*)\\b *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    Matcher match = APT_PLACE_PTN.matcher(data.strApt);
    if (match.matches()) {
      data.strApt = match.group(1);
      data.strPlace = append(data.strPlace, " - ", match.group(2));
    }
    return true;
  }
}
