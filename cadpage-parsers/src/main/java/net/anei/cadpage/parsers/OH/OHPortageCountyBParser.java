package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA39Parser;

/**
 * Portage County, OH
 */
public class OHPortageCountyBParser extends DispatchA39Parser {

  public OHPortageCountyBParser() {
    super(OHPortageCountyParser.CITY_LIST, "PORTAGE COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "garrettsvilledispatch@gmail.com";
  }
  
  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    
    // Fix a few dispatch oddities
    Matcher match = SLASH_DOTS_PTN.matcher(body);
    if (match.matches()) body = match.group(1) + '\n' + match.group(2) + '\n' + match.group(3);
    body = BROISUS_PTN.matcher(body).replaceAll("BROSIUS");
    
    if (!super.parseUntrimmedMsg(subject, body, data)) return false;
    data.strCity = OHPortageCountyParser.fixCity(data.strCity);
    return true;
  }
  private static final Pattern SLASH_DOTS_PTN = Pattern.compile("(.*)\n(.*)\\.\\.+(.*)");
  private static final Pattern BROISUS_PTN = Pattern.compile("\\bBROISUS\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String postAdjustMapAddress(String addr) {
    return OHPortageCountyParser.fixMapAddress(addr);
  }

}
