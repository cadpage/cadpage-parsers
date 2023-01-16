package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHAdamsCountyParser extends DispatchA1Parser {

  public OHAdamsCountyParser() {
    super("ADAMS COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "dispatch@adamscountyoh.gov";
  }


  private static final Pattern DASHED_CITY_PTN =
      Pattern.compile("(?<=\n)([A-Z ]+-[ A-Z]+ (?:TWP|COUNTY)|PEEBLES-MEIGS|WINCHESTER-OLIVER|WINCHESTER-TWP)(?=\n)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "AUTOMATED MESSAGE DO NOT REPLY\n");
    if (subject.length() == 0 && body.startsWith("Alert:")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      subject = body.substring(0, pt).trim();
      body = body.substring(pt+1).trim();
    }

    // We need to remove those infernal dashes from the city field

    body = body.replace(" TWP.\n", " TWP\n");
    Matcher match = DASHED_CITY_PTN.matcher(body);
    if (match.find()) {
      String city = match.group(1);
      if (city.equals("WINCHESTER-TWP")) {
        city = "WINCHESTER TWP";
      } else {
        int pt = city.indexOf('-');
        city = city.substring(0,pt).trim();
      }
      StringBuffer sb = new StringBuffer();
      match.appendReplacement(sb, city);
      match.appendTail(sb);
      body = sb.toString();
    }

    return super.parseMsg(subject, body, data);
  }

}
