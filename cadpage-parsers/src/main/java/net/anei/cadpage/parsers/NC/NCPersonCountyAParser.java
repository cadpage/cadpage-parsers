package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class NCPersonCountyAParser extends DispatchSouthernParser {

  public NCPersonCountyAParser() {
    super(NCPersonCountyParser.CITY_LIST, "PERSON COUNTY", "NC",
          DSFLAG_OPT_DISPATCH_ID | DSFLAG_ID_OPTIONAL |  DSFLAG_FOLLOW_CROSS);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = LONGS_STR_PTN.matcher(addr).replaceAll("LONGS STORE RD");
    addr = J_BREWER_PTN.matcher(addr).replaceAll("JOHN BREWER");
    addr = RL_PTN.matcher(addr).replaceAll(" ");
    return addr;
  }
  private static final Pattern LONGS_STR_PTN = Pattern.compile("\\bLONGS STR\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern J_BREWER_PTN = Pattern.compile("\\bJ BREWER\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern RL_PTN = Pattern.compile(" *\\bR/L\\b *", Pattern.CASE_INSENSITIVE);

  private static final Pattern BAD_SIG_PTN = Pattern.compile(" \\d\\d:\\d\\d:\\d\\d \\S+\\]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (BAD_SIG_PTN.matcher(body).find()) return false;
    if (!super.parseMsg(body, data)) return false;
    data.strAddress = data.strAddress.replace(" R & L ", " R/L ");
    return true;
  }
}
