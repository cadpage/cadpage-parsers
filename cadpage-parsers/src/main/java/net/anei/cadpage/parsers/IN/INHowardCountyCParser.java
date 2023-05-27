package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class INHowardCountyCParser extends DispatchA48Parser {

  public INHowardCountyCParser() {
    super(CITY_LIST, "HOWARD COUNTY", "IN", FieldType.GPS_PLACE_X, A48_NO_CODE);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return super.parseMsg(subject, ": " + body, data);
  }

  private static final Pattern COUNTY_RD_PTN = Pattern.compile("\\b[NSEW] ?\\d{3,4} ?[NSEW]\\b");

  @Override
  public String adjustMapAddress(String addr) {
    return COUNTY_RD_PTN.matcher(addr).replaceAll("COUNTY ROAD $0");
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "931 GRANDVIEW",          "+40.490160,-86.009264",
      "940 GRANDVIEW",          "+40.489924,-86.007873"

  });

  private static final String[] CITY_LIST = new String[] {

      // City
      "KOKOMO",
      "TOWNS",
      "GREENTOWN",
      "RUSSIAVILLE",

      // Former census-designated place
      "INDIAN HEIGHTS",
      "TOWNSHIPS",
      "CENTER",
      "CLAY",
      "ERVIN",
      "HARRISON",
      "HONEY CREEK",
      "HOWARD",
      "JACKSON",
      "LIBERTY",
      "MONROE",
      "TAYLOR",
      "UNION",

      // Other places
      "ALTO",
      "CASSVILLE",
      "CENTER",
      "HEMLOCK",
      "NEW LONDON",
      "OAKFORD",
      "PHLOX",
      "WEST MIDDLETON"
  };

}
