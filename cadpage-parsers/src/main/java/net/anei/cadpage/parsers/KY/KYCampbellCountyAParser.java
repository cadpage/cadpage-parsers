package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Campbell County, KY
 */
public class KYCampbellCountyAParser extends DispatchA27Parser {
  
  public KYCampbellCountyAParser() {
    super("CAMPBELL COUNTY", "KY","[A-Z]{1,3}FD|[A-Z]+\\d+|\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,do-not-reply@cccdcky.org";
  }
  
  private static final Pattern LEAD_ZERO_PTN = Pattern.compile("\\b0+(?=\\d)");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {4,}");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCallId =  LEAD_ZERO_PTN.matcher(data.strCallId).replaceAll("");
    data.strUnit =  LEAD_ZERO_PTN.matcher(data.strUnit).replaceAll("");
    data.strSupp = MSPACE_PTN.matcher(data.strSupp).replaceAll("\n");
    return true;
  }

  @Override
  public int getMapFlags() {
    
    // We really don't have enough examples to make this determination, but
    // give dispatch the benefit of doubt.
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PI_PTN.matcher(addr).replaceAll("PIKE");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PI_PTN = Pattern.compile("\bPI\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("Fairlane")) city = "Butler";
    return city;
  }
}
