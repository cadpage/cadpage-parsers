package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Muskingum County, OH
 */
public class OHMuskingumCountyAParser extends DispatchCiscoParser {
  
  public OHMuskingumCountyAParser() {
    super(CITY_LIST, "MUSKINGUM COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "ciscotext@ohiomuskingumsheriff.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.endsWith(" CTY")) {
      data.strCity = data.strCity.substring(0,data.strCity.length()-3) + "COUNTY";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {

    @Override
    public void setQual(String qual) {
      // The DispatchCiscoParser class turns on extends apt field processing
      // this conflicts with some of the the odd county and township road names
      // so we will quietly disable it.
      super.setQual("S");
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = COSH_PTN.matcher(addr).replaceAll("COSHOCTON");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern COSH_PTN = Pattern.compile("\\bCOSH\\b");

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ZANESVILLE",

    // Villages
    "ADAMSVILLE",
    "DRESDEN",
    "FRAZEYSBURG",
    "FULTONHAM",
    "GRATIOT",
    "NEW CONCORD",
    "NORWICH",
    "PHILO",
    "ROSEVILLE",
    "SOUTH ZANESVILLE",

    // Townships
    "ADAMS TWP",
    "BLUE ROCK TWP",
    "BRUSH CREEK TWP",
    "CASS TWP",
    "CLAY TWP",
    "FALLS TWP",
    "HARRISON TWP",
    "HIGHLAND TWP",
    "HOPEWELL TWP",
    "JACKSON TWP",
    "JEFFERSON TWP",
    "LICKING TWP",
    "MADISON TWP",
    "MEIGS TWP",
    "MONROE TWP",
    "MUSKINGUM TWP",
    "NEWTON TWP",
    "PERRY TWP",
    "RICH HILL TWP",
    "SALEM TWP",
    "SALT CREEK TWP",
    "SPRINGFIELD TWP",
    "UNION TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",

    // Census-designated places
    "NORTH ZANESVILLE",
    "PLEASANT GROVE",

    // Other communities
    "BLUE ROCK",
    "CHANDLERSVILLE",
    "DUNCAN FALLS",
    "EAST FULTONHAM",
    "HOPEWELL",
    "NASHPORT",
    "WHITE COTTAGE",
    
    // Other counties
    "COSHOCTON CTY",
    "GUERNSEY CTY",
    "NOBLE CTY",
    "MORGAN CTY",
    "PERRY CTY",
    "LICKING CTY"
  };
}
