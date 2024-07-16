package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;


public class OHMontgomeryCountyAParser extends DispatchH03Parser {

  public OHMontgomeryCountyAParser() {
    super("MONTGOMERY COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "@mcohiosheriff.org";
  }
  
  private static final Pattern CITY_EXT_PTN = Pattern.compile("(.*?) +\\((.*)\\)");
  
  @Override
  public String adjustMapCity(String city) {
    Matcher match = CITY_EXT_PTN.matcher(city);
    if (match.matches()) {
      city = match.group(1);
      String county = COUNTY_CODES.getProperty(match.group(2).trim());
      if (county != null) city = city + ',' + county;
    }
    return city;
  }
  
  private static final Properties COUNTY_CODES = buildCodeTable(new String[] {
      "DC", "DARKE COUNTY",
      "WC", "WARREN COUNTY"
  });
}
