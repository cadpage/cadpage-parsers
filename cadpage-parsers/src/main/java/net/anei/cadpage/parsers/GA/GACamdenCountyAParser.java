package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

/**
 * Camden County, GA
 */

public class GACamdenCountyAParser extends DispatchA57Parser {
  
  public GACamdenCountyAParser() {
    super("CAMDEN COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "911paging@camdensheriff.org";
  }
  
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("((?:^|\n) *(?:Call Time|Call Type|Address|Common Name|Closest Intersection|Additional Location Info|Assigned Units|Quadrant|Primary Incident|Narrative)) ");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (body.startsWith("Time ")) {
      body = MISSING_COLON_PTN.matcher("Call " + body.replace("Closet Intersetion", "Closest Intersection")).replaceAll("$1:");
    }
    return super.parseMsg(body, data);
  };
}
