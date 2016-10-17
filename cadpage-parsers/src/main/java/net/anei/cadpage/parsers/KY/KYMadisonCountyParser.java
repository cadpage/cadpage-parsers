package net.anei.cadpage.parsers.KY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Madison County, KY
 */
public class KYMadisonCountyParser extends DispatchA27Parser {
  
  public KYMadisonCountyParser() {
    super("MADISON COUNTY", "KY", "[A-Z]{1,4}\\d*");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,noreply@cisusa.org";
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    Matcher match = I75_MM_PTN.matcher(address);
    if (match.matches()) address = match.group(1);
    return super.adjustGpsLookupAddress(address);
  }
  private static final Pattern I75_MM_PTN = Pattern.compile("(I75 \\d+) [NSEW] BOUND");

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "I75 65",  "37.42060,-84.34230",
      "I75 66",  "37.43402,-84.34243",
      "I75 67",  "37.44785,-84.33669",
      "I75 68",  "37.46223,-84.33548",
      "I75 69",  "37.47657,-84.33357",
      "I75 70",  "37.49093,-84.32986",
      "I75 71",  "37.50488,-84.32529",
      "I75 72",  "37.51852,-84.31963",
      "I75 73",  "37.53268,-84.32063",
      "I75 74",  "37.54657,-84.31758",
      "I75 75",  "37.56108,-84.31574",
      "I75 76",  "37.57558,-84.31394",
      "I75 77",  "37.59017,-84.31381",
      "I75 78",  "37.60477,-84.31442",
      "I75 79",  "37.61940,-84.31493",
      "I75 80",  "37.63372,-84.31632",
      "I75 81",  "37.64805,-84.31364",
      "I75 82",  "37.66238,-84.31090",
      "I75 83",  "37.67685,-84.31217",
      "I75 84",  "37.69108,-84.31517",
      "I75 85",  "37.70532,-84.31888",
      "I75 86",  "37.71963,-84.32160",
      "I75 87",  "37.73413,-84.32363",
      "I75 88",  "37.74835,-84.32113",
      "I75 89",  "37.76260,-84.31792",
      "I75 90",  "37.77690,-84.31634",
      "I75 91",  "37.79048,-84.32275",
      "I75 92",  "37.80483,-84.32443",
      "I75 93",  "37.81917,-84.32380",
      "I75 94",  "37.83383,-84.32305",
      "I75 95",  "37.84720,-84.32923",
      "I75 96",  "37.86135,-84.33220",
      "I75 97",  "37.87580,-84.33322",
      "I75 98",  "37.88616,-84.34257",
      "I75 99",  "37.89678,-84.35446",
      "I75 100", "37.91025,-84.36138",
      "I75 101", "37.92318,-84.37008",
      "I75 102", "37.93628,-84.37782",
      "I75 103", "37.94960,-84.38571",
      "I75 104", "37.96370,-84.38873"
  });
 
}
