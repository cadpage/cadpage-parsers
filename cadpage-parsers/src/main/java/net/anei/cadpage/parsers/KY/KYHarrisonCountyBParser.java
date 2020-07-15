package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;



public class KYHarrisonCountyBParser extends DispatchA65Parser {

  public KYHarrisonCountyBParser() {
    super(CITY_LIST, "HARRISON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " PD");
    return true;
  }

  private static final String[] CITY_LIST = new String[]{

    "BERRY",
    "CORINTH",
    "CYNTHIANA",

//Unincorporated communities

    "BOYD",
    "BRECKINRIDGE",
    "BROADWELL",
    "BUENA VISTA",
    "COLVILLE",
    "CONNERSVILLE",
    "HOOKTOWN",
    "LAIR",
    "LEES LICK",
    "LEESBURG",
    "KELAT",
    "MORNINGGLORY",
    "ODDVILLE",
    "POINDEXTER",
    "RUDDELS MILLS",
    "RUTLAND",
    "SHADYNOOK",
    "SHAWHAN",
    "SUNRISE",

    // Pendleton County
    "FALMOUTH",

    // Bourboun County
    "PARIS",
    "PARIS PD"
  };
}