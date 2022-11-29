package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TXDallasCountyCParser extends DispatchH03Parser {

  public TXDallasCountyCParser() {
    super("DALLAS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "NTEC@motocad.local,cad@ntecc.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MARKER = Pattern.compile("([A-Za-z ]* Notification)\n");
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) {
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        subject = match.group(1);
        body = body.substring(match.end()).trim();
      }
    }
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("DFW AIRPORT")) city = "DALLAS/FORT WORTH AIRPORT";
    return super.adjustMapCity(city);
  }
}
