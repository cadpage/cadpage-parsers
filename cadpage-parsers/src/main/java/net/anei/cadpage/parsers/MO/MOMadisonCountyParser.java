package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOMadisonCountyParser extends DispatchA33Parser {
  public MOMadisonCountyParser() {
    super(CITY_LIST, "MADISON COUNTY", "MO", A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "MADISONCO911@OMNIGO.COM,MADISONCO911@PUBLICSAFETYSOFTWARE.NET";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
//
//  @Override
//  protected boolean parseMsg(String body, Data data) {
//    if (!super.parseMsg(body, data)) return false;
//    if (data.strCross.endsWith(", MO")) {
//      data.strCity = data.strCross.substring(0, data.strCross.length()-4).trim();
//      data.strState = "MO";
//      data.strCross = "";
//    } else if (data.strCross.equals("MO")) {
//      data.strState = "MO";
//      data.strCross = "";
//    }
//    else if (isCity(data.strCross)) {
//      data.strCity = data.strCross;
//      data.strCross = "";
//    }
//    return true;
//  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "FREDERICKTOWN",

      // Town
      "MARQUAND",

      // Villages
      "COBALT",
      "JUNCTION CITY",

      // Census-designated places
      "CHEROKEE PASS",
      "MINE LA MOTTE",

      // Other unincorporated communities
      "ALLBRIGHT",
      "BUCKHORN",
      "CATHERINE PLACE",
      "CORNWALL",
      "FARO",
      "FRENCH MILLS",
      "HAHNS MILL",
      "HIGDON",
      "JEWETT",
      "LANCE",
      "MILLCREEK",
      "OAK GROVE",
      "ROSELLE",
      "SACO",
      "SAINT MICHEL",
      "SILVER MINE",
      "TIN MOUNTAIN",
      "TWELVEMILE",
      "ZION"
  };
}
