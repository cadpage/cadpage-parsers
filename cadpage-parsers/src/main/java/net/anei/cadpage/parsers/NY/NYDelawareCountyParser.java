package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class NYDelawareCountyParser extends DispatchB2Parser {
  
  public NYDelawareCountyParser() {
    super("DELCO911:||DELCO 911:", CITY_CODES, "DELAWARE COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "delco911@co.delaware.ny.us";
  }
  
  @Override
  protected CodeSet buildCallList() {
    return null;
  }

  @Override
  protected boolean parseAddrField(String field, Data data) {
    if (!super.parseAddrField(field, data)) return false;
    if (data.strCity.equals("FLEISCHMANN")) data.strCity = "FLEISCHMANNS";
    return true;
  }

  private static final String[] CITY_CODES = new String[]{
    "ANDES",
    "BOVINA",
    "COLCHESTER",
    "DAVENPORT",
    "DELHI",
    "DEPOSIT",
    "DOWNSVILLE",
    "FLEISCHMANN",
    "FLEISCHMANNS",
    "FLEISCHMANNS VILLAGE",
    "FRANKLIN",
    "HAMDEN",
    "HANCOCK",
    "HARPERSFIELD",
    "HOBART",
    "KORTRIGHT",
    "MARGARETVILLE",
    "MASONVILLE",
    "MEREDITH",
    "MIDDLETOWN",
    "ROXBURY",
    "SIDNEY",
    "STAMFORD",
    "TOMPKINS",
    "WALTON",
    
    // Greene County
    "HALCOTT",
    
    // Ulster  County
    "HARDENBURG"
  };
}
