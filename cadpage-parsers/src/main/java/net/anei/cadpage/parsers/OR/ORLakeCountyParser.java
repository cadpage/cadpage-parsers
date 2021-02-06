package net.anei.cadpage.parsers.OR;


import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;



public class ORLakeCountyParser extends DispatchA22Parser {

  public ORLakeCountyParser() {
    super(CITY_CODES, "LAKE COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "Dispatch@LCETS.net,Dispatch@psnet.us,donotreply@townoflakeview.org";
  }

  private static final Pattern CV_PTN = Pattern.compile("\\bCV\\b");

  @Override
  public String adjustMapAddress(String addr) {
    addr = CV_PTN.matcher(addr).replaceAll("CHRISTMAS VALLEY");
    return addr;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CV",       "CHRISTMAS VALLEY"
  });
}
