package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA85Parser;

public class ORGrantCountyParser extends DispatchA85Parser {

  public ORGrantCountyParser() {
    super(CITY_CODES, "GRANT COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "DispatchText@grantcounty-or.gov";
  }

  private static final Pattern USFS_PTN = Pattern.compile("\\bUSFS(?: RD)?\\b");

  @Override
  public String adjustMapAddress(String addr) {
    return USFS_PTN.matcher(addr).replaceAll("NF");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BAT", "BATES",
      "CAN", "CANYON CITY",
      "DAL", "DALE",
      "DAY", "DAYVILLE",
      "FOX", "FOX",
      "GRA", "GRANITE",
      "JOH", "JOHN DAY",
      "KIM", "KIMBERLY",
      "LON", "LONG CREEK",
      "MON", "MONUMENT",
      "MT.", "MT VERNON",
      "PRA", "PRAIRIE CITY",
      "RIT", "RITTER",
      "SEN", "SENECA",
      "UNI", "UNITY"
  });
}
