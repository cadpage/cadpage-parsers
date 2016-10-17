package net.anei.cadpage.parsers.ZCABC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA51Parser;
/**
 * Nanaimo, BC, Canada
 */
public class ZCABCNanaimoParser extends DispatchA51Parser {
  
  public ZCABCNanaimoParser() {
    super("NANAIMO", "BC");
  }

  @Override
  public String getFilter() {
    return "ciFirecomm_do_not_reply@nanaimo.ca";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = TRANS_CANADA_PTN.matcher(addr).replaceAll("ISLAND");
    return addr;
  }
  Pattern TRANS_CANADA_PTN = Pattern.compile("\\bTRANS[- ]CANADA\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("New Incident Alert")) return false;
    if (!super.parseMsg(body,  data)) return false;
    if (data.strCity.equals("GABRIOLA ISLD")) data.strCity = "GABRIOLA";
    return true;
  }
}
