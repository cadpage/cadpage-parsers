package net.anei.cadpage.parsers.NC;


import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCHarnettCountyBParser extends DispatchSouthernParser {

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("\\b\\d{1,2}:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d\\d\\b|\\bGINO\\(EN\\)");

  public NCHarnettCountyBParser() {
    super(NCHarnettCountyParser.CITY_LIST, "HARNETT COUNTY", "NC",
           DSFLAG_OPT_DISPATCH_ID | DSFLAG_LEAD_PLACE | DSFLAG_CROSS_NAME_PHONE | DSFLAG_ID_OPTIONAL);
  }

  @Override
  public String getFilter() {
    return "@dunn-nc.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Eliminate NCHarnettCountyD alerts
    if (body.startsWith("AD:") || body.startsWith("PN:") ||
        body.startsWith("Received:")) return false;

    if (RUN_REPORT_PTN.matcher(body).find()) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    return super.parseMsg(body, data);
  }

  @Override
  public String adjustMapAddress(String address) {
    address = address.replace("THREE BRIDGE RD", "3 BRIDGE RD");
    return super.adjustMapAddress(address);
  }
}