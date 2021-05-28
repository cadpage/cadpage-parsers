package net.anei.cadpage.parsers.CO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class COCostillaCountyParser extends DispatchH03Parser {

  public COCostillaCountyParser() {
    super("COSTILLA COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return ".AL@csp.noreply";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;

    // They do a lot of weird abbreviating of addresses that needs to be cleaned up
    data.strAddress = fixAddress(data.strAddress);
    return true;
  }

  private static final Pattern DDMP_PTN = Pattern.compile("(\\d+)(MP)");
  private static final Pattern HDD_PTN = Pattern.compile("\\bH(\\d+)");
  private static final Pattern CCRDD_PTN = Pattern.compile("CCR *(\\d+|[A-Z]\\b)");
  private static final Pattern CCRXDD_PTN = Pattern.compile("CCR *([A-Z])(\\d+)");

  private String fixAddress(String addr) {
    addr = DDMP_PTN.matcher(addr).replaceAll("$1 $2");
    addr = HDD_PTN.matcher(addr).replaceAll("HWY $1");
    addr = CCRDD_PTN.matcher(addr).replaceAll("CR $1");
    addr = CCRXDD_PTN.matcher(addr).replaceAll("CR $1 $2");
    return addr;
  }
}
