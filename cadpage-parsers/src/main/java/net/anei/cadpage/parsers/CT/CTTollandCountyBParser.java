package net.anei.cadpage.parsers.CT;

import java.util.regex.Pattern;


public class CTTollandCountyBParser extends CTNewHavenCountyBParser {
  
  public CTTollandCountyBParser() {
    super(CTTollandCountyParser.CITY_LIST, "TOLLAND COUNTY", "CT");
  }
  
  @Override
  public String getFilter() {
    return "paging@easthavenfire.com,pubsafetypaging@uconn.edu";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = GILBERT_EXT.matcher(address).replaceAll("GILBERT RD EXT");
    return super.adjustMapAddress(address);
  }
  private static final Pattern GILBERT_EXT = Pattern.compile("\\bGILBERT EXT\\b", Pattern.CASE_INSENSITIVE);
}
