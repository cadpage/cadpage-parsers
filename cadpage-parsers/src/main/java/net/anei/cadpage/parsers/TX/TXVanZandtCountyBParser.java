package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXVanZandtCountyBParser extends FieldProgramParser {

  public TXVanZandtCountyBParser() {
    super(CITY_LIST, "VAN ZANDT COUNTY", "TX",
          "ADDR/S EMPTY EMPTY EMPTY CALL! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split(";"), data)) return false;
    return !data.strCity.isEmpty();
  }

  private static final String[] CITY_LIST = new String[]{

    "VAN ZANDT COUNTY",

    // Cities
    "CANTON",
    "EDGEWOOD",
    "EDOM",
    "FRUITVALE",
    "GRAND SALINE",
    "VAN",
    "WILLS POINT",

    // Census-designated places
    "BEN WHEELER",
    "CALLENDER LAKE",
    "MYRTLE SPRINGS",

    // Other unincorporated communities
    "ALSA",
    "COLFAX",
    "MARTINS MILL",
    "MIDWAY",
    "OAKLAND",
    "PHALBA",
    "PRIMROSE",
    "SILVER LAKE",
    "TUNDRA",
    "WALTON",
    "WENTWORTH",
    "WISE"
  };
}
