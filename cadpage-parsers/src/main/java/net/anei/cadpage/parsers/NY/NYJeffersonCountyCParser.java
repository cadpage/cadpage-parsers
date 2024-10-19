package net.anei.cadpage.parsers.NY;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NYJeffersonCountyCParser extends DispatchA19Parser {

  public NYJeffersonCountyCParser() {
    super(CITY_CODES, "JEFFERSON COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "fire@co.jefferson.ny.us,@jeffersoncountyny.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    int pt = data.strCity.indexOf('(');
    if (pt >= 0) data.strCity = data.strCity.substring(0,pt).trim();
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CWT", "WATERTOWN",
      "FTD", "FORT DRUM",
      "OTH", "",
      "TAD", "ADAMS",
      "TAL", "ALEXANDRIA",
      "TAN", "ANTWERP",
      "TBR", "BROWNVILLE",
      "TCH", "CHAMPION", // unconfirmed
      "TCL", "CLAYTON", // unconfirmed
      "TCV", "CAPE VINCENT", // unconfirmed
      "TEL", "ELLISBURG",
      "THO", "HOUNSFIELD",
      "TLE", "LE REY",
      "TLO", "LORRAINE",
      "TLY", "LYME", // unconfirmed
      "TOR", "ORLEANS",
      "TPA", "PAMELIA",
      "TPH", "PHILADELPHIA",
      "TRO", "RODMAN",
      "TTH", "THERESA",
      "TWT", "WATERTOWN",
      "TWI", "WILNA", // unconfirmed
      "TWO", "WORTH", // unconfirmed
      "VAD", "ADAMS",
      "VAL", "ALEXANDRIA BAY",
      "VAN", "ANTWERP",
      "VBL", "BLACK RIVER", // unconfirmed
      "VBR", "BROWNVILLE", // unconfirmed
      "VCA", "CARTHAGE",
      "VCH", "CHAUMONT", // unconfirmed
      "VCL", "CLAYTON", // unconfirmed
      "VCV", "CAPE VINCENT", // unconfirmed
      "VDE", "DEFERIET", // unconfirmed
      "VDX", "DEXTER", // unconfirmed
      "VEL", "ELLISBURG", // unconfirmed
      "VEM", "EVANS MILLS",
      "VGL", "GLEN PARK", // unconfirmed
      "VGP", "GLEN PARK", // unconfirmed
      "VMA", "MANNSVILLE", // unconfirmed
      "VPH", "PHILADELPHIA",
      "VSA", "SACKETS HARBOR", // unconfirmed
      "VSH", "SACKETS HARBOR", // unconfirmed
      "VTH", "THERESA",
      "VWC", "WEST CARTHAGE"  // unconfirmed
  });

}
