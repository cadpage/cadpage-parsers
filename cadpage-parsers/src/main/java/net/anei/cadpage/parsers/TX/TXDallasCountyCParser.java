package net.anei.cadpage.parsers.TX;

import java.util.Properties;

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

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (subject.isEmpty() && body.startsWith("Incident Notification\n")) {
      subject = body.substring(0,21);
      body = body.substring(22).trim();
    }
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("DFW AIRPORT")) city = "DALLAS/FORT WORTH AIRPORT";
    return super.adjustMapCity(city);
  }
}
