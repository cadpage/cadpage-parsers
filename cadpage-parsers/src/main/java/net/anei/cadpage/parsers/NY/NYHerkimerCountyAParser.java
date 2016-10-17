package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB3Parser;



public class NYHerkimerCountyAParser extends DispatchB3Parser {

  private static final Pattern CITY_SUFFIX = Pattern.compile("^VILLAGE\\b", Pattern.CASE_INSENSITIVE);
  
  public NYHerkimerCountyAParser() {
    super(NYHerkimerCountyBParser.CITY_LIST, "HERKIMER COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "HC911@herkimercounty.org,911Center@mydomain.com";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.contains(">")) return false;
    body = body.replaceAll("\n", " ").replaceAll(" Grids:,, NY ", " ");
    body = stripFieldEnd(body, " MAP:");
    if (!super.parseMsg(subject, body, data)) return false;
    
    // See if city suffix has bled over into name
    Matcher match = CITY_SUFFIX.matcher(data.strName);
    if (match.find()) data.strName = data.strName.substring(match.end()).trim();
    NYHerkimerCountyBParser.fixCity(data);
    return true;
  }
}
