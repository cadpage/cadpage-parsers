package net.anei.cadpage.parsers.MT;



import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA16Parser;
/**
 * Stillwater County, MT
 */
public class MTStillwaterCountyParser extends DispatchA16Parser {
  
  public MTStillwaterCountyParser() {
    super(CITY_LIST, "STILLWATER COUNTY", "MT");
  }

  @Override
  public String getFilter() {
    return "cbrophy@scsomt.org";
  }
  
  private static final Pattern EXTRA_SUFFIX_PTN = Pattern.compile("\\b(AVE|ST) ([NS][EW]) \\1\\b");
  private static final Pattern HWY_10_PTN = Pattern.compile("\\b(?:HIGHWAY 10(?: HWY)?|(?:HIGHWAY )?HWY 10)\\b");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    // Clean up some odd address conventions
    data.strAddress = EXTRA_SUFFIX_PTN.matcher(data.strAddress).replaceAll("$1 $2");
    data.strAddress = HWY_10_PTN.matcher(data.strAddress).replaceAll("OLD US 10");
    return true;
  }


  private static final String[] CITY_LIST= new String[]{
  "ABSAROKEE",
  "BEEHIVE",
  "COLUMBUS",
  "DEAN",
  "FISHTAIL",
  "LIMESTONE",
  "MOLT",
  "NYE",
  "PARK CITY",
  "RAPELJE",
  "RAPIDS",
  "REED POINT",
  "SPRINGTIME",
  "WHEAT BASIN"

  };
}
