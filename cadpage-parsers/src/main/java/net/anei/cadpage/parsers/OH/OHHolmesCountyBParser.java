package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHHolmesCountyBParser extends FieldProgramParser {
  
  public OHHolmesCountyBParser() {
    super(CITY_LIST, "HOLMES COUNTY", "OH", 
          "UNIT CALL ADDR! APT? CITY? INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] parts = body.split("\n");
    if (parts.length != 3) return false;
    if (!parts[0].contains("Holmes County Sheriff's Office")) return false;
    if (!parts[1].equals("Dispatch")) return false;
    return parseFields(parts[2].split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("(?i:(?:APT|LOT|RM|ROOM) *(.*)|\\d{1,4}[A-Z]?|[A-Z])", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{

    "BERLIN TWP",
      "BERLIN",

    "CLARK TWP",
      "BALTIC",
      "CHARM",

    "FARMERSTOWN TWP",
      "UNIONVILLE",

    "HARDY TWP",
      "MILLERSBURG",

    "KILLBUCK TWP",
      "KILLBUCK",

    "KNOX TWP",
      "NASHVILLE",

    "MECHANIC TWP",
      "LAKE BUCKHORN",
      "BECKS MILLS",
      "SALTILLO",

    "MONROE TWP",
      "WELCOME",

    "PAINT TWP",
      "WINESBURG",

    "PRAIRIE TWP",
      "HOLMESVILLE",

    "RICHLAND TWP",
      "GLENMONT",
      "STILLWELL",

    "RIPLEY TWP",
      "BIG PRAIRIE",

    "SALT CREEK TWP",
      "MT HOPE",

    "WALNUT CREEK TWP",
      "WALNUT CREEK",
      "TRAIL",

    "WASHINGTON TWP",
      "LOUDONVILLE",
      "NASHVILLE",
      "LAKEVILLE",
      
    // Montgomery County
    "DAYTON"
  };

}
