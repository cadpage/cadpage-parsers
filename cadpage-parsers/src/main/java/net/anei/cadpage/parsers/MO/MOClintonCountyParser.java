package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOClintonCountyParser extends DispatchA25Parser {
  
  private static final Pattern TOWN_AND_COUNTRY_PTN0 = Pattern.compile("\\b(TOWN) +& +(COUNTRY)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TOWN_AND_COUNTRY_PTN1 = Pattern.compile("\\b(TOWN) +(AND) +(COUNTRY)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TOWN_AND_COUNTRY_PTN2 = Pattern.compile("\\b(TOWN)_(AND)_(COUNTRY)\\b", Pattern.CASE_INSENSITIVE);
  
  public MOClintonCountyParser() {
    super("CLINTON COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "@clintoncosheriff.org,@plattsburgfire.org";  
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = TOWN_AND_COUNTRY_PTN0.matcher(body).replaceAll("$1 and $2");
    body = adjustMapAddress(body);
    if (!super.parseMsg(subject, body, data)) return false;
    data.strAddress = postAdjustMapAddress(data.strAddress);
    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    return TOWN_AND_COUNTRY_PTN1.matcher(addr).replaceAll("$1_$2_$3");
  }
  
  @Override
  public String postAdjustMapAddress(String addr) {
    return TOWN_AND_COUNTRY_PTN2.matcher(addr).replaceAll("$1 $2 $3");
  }
}
