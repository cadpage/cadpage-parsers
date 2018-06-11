package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;


public class MNScottCountyParser extends DispatchA43Parser {
  
  public MNScottCountyParser() {
    super(CITY_LIST, "SCOTT COUNTY", "MN");
    setupSpecialStreets(
        "OLD HIGHWAY 13 BLVD",
        "OLD HIGHWAY 169 BLVD", 
        "TRAIL OF DREAMS NW");
  }
 
  @Override
  protected boolean parseMsg(String body, Data data) {
    // TODO Auto-generated method stub
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equalsIgnoreCase("SAN FRANCISO TWP")) data.strCity = "SAN FRANCISCO TWP";
    return true;
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.toUpperCase().startsWith("TO ")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "BELLE PLAINE",
    "ELKO NEW MARKET",
    "JORDAN",
    "NEW PRAGUE",
    "PRIOR LAKE",
    "SAVAGE",
    "SHAKOPEE",

    // Townships
    "BELLE PLAINE TOWNSHIP",
    "BLAKELEY TOWNSHIP",
    "CEDAR LAKE TOWNSHIP",
    "CREDIT RIVER TOWNSHIP",
    "HELENA TOWNSHIP",
    "JACKSON TOWNSHIP",
    "LOUISVILLE TOWNSHIP",
    "NEW MARKET TOWNSHIP",
    "SAND CREEK TOWNSHIP",
    "SPRING LAKE TOWNSHIP",
    "ST LAWRENCE TOWNSHIP",
    "SAINT LAWRENCE TOWNSHIP",

    // Unincorporated communities
    "BLAKELEY",
    "CEDAR LAKE",
    "HELENA",
    "LYDIA",
    "MARYSTOWN",
    "MUDBADEN",
    "SPRING LAKE",
    "ST BENEDICT",
    "ST PATRICK",
    "UNION HILL",
    
    // Carver County
    "SAN FRANCISO TWP",
    "SAN FRANCISCO TWP",
    
    // Le Sueur County
    "DERRYNANE TOWNSHIP",
    "LANESBURGH",
    
    // Sibley County
    "FAXON TWP",
    
    "DAKOTA COUNTY",
    "CARVER COUNTY",
    "HENNEPIN COUNTY",
    "LE SUEUR COUNTY",
    "RICE COUNTY",
    "RAMSEY COUNTY",
    "SIBLEY COUNTY"
  };
}
