package net.anei.cadpage.parsers.MN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Dodge County, MN
 */

public class MNDodgeCountyParser extends DispatchA27Parser {
  
  public MNDodgeCountyParser() {
    super("DODGE COUNTY", "MN", "\\d{8}|RDM");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,admin.strator@co.dodge.mn.us";
  }
  
  private static final Pattern UNIT_ZEROS_PTN = Pattern.compile("(?:^|(?<= ))0+(?=.)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strUnit = UNIT_ZEROS_PTN.matcher(data.strUnit).replaceAll("");
    return true;
  }
  
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "60665 226 AVE",                        "+44.065877,-92.784994",
      "60678 226 AVE",                        "+44.065870,-92.784990",
      "60747 226 AVE",                        "+44.066817,-92.782867",
      "61055 226 AVE",                        "+44.064850,-92.788800",
      "61123 226 AVE",                        "+44.064850,-92.788803",
      "61189 226 AVE",                        "+44.064850,-92.788785",
      "61311 226 AVE",                        "+44.062333,-92.788767",
      "24304 555TH ST",                       "+44.145333,-92.750888",
      "416 9TH ST W",                         "+44.071160,-92.761986",
      "1000 BLANCH CT",                       "+44.072252,-92.754519",
      "1003 BLANCH CT",                       "+44.072405,-92.754666",
      "141 COUNTY RD 12",                     "+44.061981,-92.757681"
  });
  
}
