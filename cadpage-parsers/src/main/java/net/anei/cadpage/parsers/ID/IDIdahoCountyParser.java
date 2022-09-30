package net.anei.cadpage.parsers.ID;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDIdahoCountyParser extends DispatchA19Parser {

  public IDIdahoCountyParser() {
    super(CITY_CODES, "IDAHO COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "spillmannotify@idahocounty.org";
  }

private static final Properties CITY_CODES = buildCodeTable(new String[] {
    "CLE", "Clearwater", //
    "COT", "Cottonwood",
    "CPD", "Cottonwood",
    "CRA", "Craigmont",
    "DIX", "Dixie", //
    "ELC", "Elk City",
    "ELK", "Elk City",
    "ELW", "Elk City", //
    "FEN", "Fenn", //
    "FER", "Ferdinand",
    "GPD", "Grangeville",
    "GRA", "Grangeville",
    "GRE", "Greencreek", //
    "HAR", "Harpster", //
    "KAM", "Kamiah",
    "KEU", "Keuterville",
    "KOO", "Kooskia",
    "KPD", "Kooskia",
    "KST", "Stites",
    "LOW", "Lowell", //
    "LUC", "Lucile",
    "MCC", "McCall",
    "NEW", "New Meadows",
    "NEZ", "Nez Perce County",
    "POL", "Pollock",
    "POW", "Powell Junction", // ???
    "Rig City Limits", "Riggins",
    "RIG", "Riggins",
    "RPD", "Riggins",
    "STI", "Stites",
    "WHI", "White Bird",
    "WIN", "Winchester"
});

}
