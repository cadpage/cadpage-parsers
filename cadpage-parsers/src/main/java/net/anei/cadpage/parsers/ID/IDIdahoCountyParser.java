package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDIdahoCountyParser extends DispatchA19Parser {

  public IDIdahoCountyParser() {
    super("IDAHO COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "spillmannotify@idahocounty.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;

    // City code is just not reliable
    data.strCity = "";
    return true;
  }

//  "NEW" : "New Meadows",
//  "KST" : "Stites",
//  "KPD" : "Kooskia",
//  "KOO" : "Kooskia",
//  "RIG" : "Riggins",
//  "ELC" : "Elk City",
//  "KAM" : "Kamiah",
//  "CRA" : "Craigmont",
//  "GRA" : "Grangeville",
//  "WHI" : "White Bird",
//  "GPD" : "Grangeville",
//  "MCC" : "McCall",
//  "FER" : "Ferdinand",
//  "CPD" : "Cottonwood",
//  "KEU" : "Keuterville",
//  "STI" : "Stites",
//  "LUC" : "Lucile",
//  "WIN" : "Winchester",
//  "ELK" : "Elk City",
//  "Rig City Limits" : "Riggins",
//  "COT" : "Cottonwood",
//  "POL" : "Pollock",
//  "RPD" : "Riggins"

}
