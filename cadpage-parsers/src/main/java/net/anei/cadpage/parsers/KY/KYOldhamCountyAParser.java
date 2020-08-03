package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Oldham County, KY
 */
public class KYOldhamCountyAParser extends DispatchEmergitechParser {

  public KYOldhamCountyAParser() {
    super("DISPATCH:", KYOldhamCountyParser.CITY_LIST, "OLDHAM COUNTY", "KY");
    addSpecialWords("COLTON");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@oldhamcounty.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equalsIgnoreCase("LAGRANGE")) data.strCity =  "LA GRANGE";

    // They use the unit field for a dispatcher ID, which we are not interested in
    data.strUnit = "";
    return true;
  }

  private static final Pattern THREE_LAKES_RD_PTN = Pattern.compile("\\bTHREE LAKES RD\\b");

  @Override
  public String adjustMapAddress(String addr) {
    addr = THREE_LAKES_RD_PTN.matcher(addr).replaceAll("3 LAKES RD");
    return super.adjustMapAddress(addr);
  }
}
