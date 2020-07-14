package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Franklin County, GA
 */
public class GAFranklinCountyAParser extends DispatchSouthernParser {

  public GAFranklinCountyAParser() {
    super(CITY_LIST, "FRANKLIN COUNTY", "GA",
          DSFLG_OPT_DISP_ID | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_NAME | DSFLG_OPT_ID | DSFLG_TIME);
    setupSaintNames("MARYS");
  }

  @Override
  public String getFilter() {
    return "FC911@franklincountyga.com,FC911@gumlog.net,7064911018@mms.firstnet-mail.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("FC911:")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (NOT_APT_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }
  private static final Pattern NOT_APT_PTN = Pattern.compile("I[- ].*", Pattern.CASE_INSENSITIVE);

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_IMPLIED_INTERSECT;
  }

  @Override
  public String adjustMapAddress(String sAddress,  boolean cross) {
    if (cross) {
      int pt = sAddress.indexOf('/');
      if (pt >= 0) sAddress = sAddress.substring(0,pt).trim();
      if (checkAddress(sAddress) == STATUS_FULL_ADDRESS) {
        pt = sAddress.indexOf(' ');
        if (pt >= 0) sAddress = sAddress.substring(pt+1).trim();
      }
    }
    return super.adjustMapAddress(sAddress, cross);
  }
  private static final String[] CITY_LIST = new String[]{
    "CANON",
    "CARNESVILLE",
    "FRANKLIN SPRINGS",
    "GUMLOG",
    "LAVONIA",
    "MARTIN",
    "ROYSTON",

    // Jackson County
    "COMMERCE",

    // Stephens County
    "TOCCOA"
  };

}
