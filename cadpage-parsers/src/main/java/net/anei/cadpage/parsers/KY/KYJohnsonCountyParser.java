package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Johnson County, KY
 */

public class KYJohnsonCountyParser extends DispatchSPKParser {

  public KYJohnsonCountyParser() {
    super("JOHNSON COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "CADPJC911@CityofPaintsville.net";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strMap = data.strPlace;
    data.strPlace = "";
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = CRK_PTN.matcher(addr).replaceAll("CREEK");
    return super.adjustMapAddress(addr);
  }
  
  private static final Pattern CRK_PTN = Pattern.compile("\\bCRK\\b", Pattern.CASE_INSENSITIVE);

}
