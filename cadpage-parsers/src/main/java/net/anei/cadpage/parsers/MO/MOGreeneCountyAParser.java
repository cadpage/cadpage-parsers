package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;



public class MOGreeneCountyAParser extends DispatchPrintrakParser {
  
  private static final Pattern FR_PTN = Pattern.compile("\\bFR(?= *\\d)", Pattern.CASE_INSENSITIVE);
  private static final Pattern SHXX_PTN = Pattern.compile("\\b(SH)([A-Z]{1,2}|\\d+)\\b");
  
  public MOGreeneCountyAParser() {
    super("GREENE COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "grnpage@springfieldmo.gov";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.length() == 0 && data.strName.equals("CHRISTIAN COUNTY")) {
      data.strCity = data.strName;
      data.strName = "";
    }
    if (data.strCity.equals("CHRISTIAN COUNT")) data.strCity = "CHRISTIAN COUNTY";
    return true;
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    sAddress = SHXX_PTN.matcher(sAddress).replaceAll("MO $2");
    sAddress = FR_PTN.matcher(sAddress).replaceAll(" FARM RD ");
    sAddress = sAddress.trim().replaceAll("  +", " ");
    return sAddress;
  }
  
  
}
