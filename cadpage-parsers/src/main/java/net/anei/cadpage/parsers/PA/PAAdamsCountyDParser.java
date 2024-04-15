package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class PAAdamsCountyDParser extends DispatchA57Parser {

  public PAAdamsCountyDParser() {
    super("ADAMS COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "CAD@adams911.com";
  }

  private static final Pattern NATURE_OF_CALL_PTN = Pattern.compile("N ?a ?t ?u ?r ?e  ?o ?f  ?C ?a ?l ?l ?:");
  private static final Pattern ADDITIONAL_LOCATION_INFO_PTN = Pattern.compile("Additional ?Location ?Info:");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Try to unscramble this mess ...
    if (body.length() < 65) return false;
    if (body.charAt(64) == ' ') {
      int len = body.length();
      body = NATURE_OF_CALL_PTN.matcher(body).replaceFirst("Nature of Call:");
      if (body.length() != len) {
        body = stripBlank(body, 193);
        body = stripBlank(body, 127);
        body = ADDITIONAL_LOCATION_INFO_PTN.matcher(body).replaceFirst("Additional Location Info:");
      }
    }
    body = body.replace("0A", "\n");
    return super.parseMsg(body, data);
  }

  private String stripBlank(String body, int pos) {
    if (body.length() > pos && body.charAt(pos) == ' ') {
      body = body.substring(0, pos) + body.substring(pos+1);
    }
    return body;
  }
}
