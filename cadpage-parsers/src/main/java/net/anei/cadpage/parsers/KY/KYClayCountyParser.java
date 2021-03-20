package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYClayCountyParser extends DispatchA74Parser {

  public KYClayCountyParser() {
    super("CLAY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Dispatch@clayky911.com";
  }

  private static final Pattern APT_ADDR_PTN = Pattern.compile("(\\d+[A-Z]?) - (\\d+.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strApt.isEmpty()) {
      Matcher match = APT_ADDR_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strApt = match.group(1);
        data.strAddress = match.group(2);
      }
    }
    return true;
  }

}