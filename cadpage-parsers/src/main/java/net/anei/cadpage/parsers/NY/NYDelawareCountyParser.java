package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;


public class NYDelawareCountyParser extends DispatchBParser {
  
  public NYDelawareCountyParser() {
    super(CITY_CODES, "DELAWARE COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "delco911@co.delaware.ny.us";
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    if (!body.startsWith("DELCO911:")) return false;
    int pt = body.indexOf('>', 10);
    if (pt < 0 || pt > 20) return false;
    return true;
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
    "WALTON"
  };
}
