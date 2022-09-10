package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class TXMcLennanCountyBParser extends DispatchA41Parser {

  public TXMcLennanCountyBParser() {
    super(CITY_CODES, "MCLENNAN COUNTY", "TX", "[A-Z]{2,4}");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PK_PARKWAY;
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEL", "BELLMEAD",
      "BH",  "BEVERLY HILLS",
      "BRU", "BRUCEVILLE-EDDY",
      "CRA", "CRAWFORD",
      "GHO", "GHOLSON",
      "GOL", "GOLINDA",
      "HAL", "HALLSBURG",
      "HEW", "HEWITT",
      "LER", "LEROY",
      "LLV", "LACY LAKEVIEW",
      "LOR", "LORENA",
      "MAR", "MART",
      "MCG", "MCGREGOR",
      "MOO", "MOODY",
      "RIE", "RIESEL",
      "ROB", "ROBINSON",
      "ROS", "ROSS",
      "VAL", "VALLEY MILLS",
      "WAC", "WACO",
      "WES", "WEST",
      "WOO", "WOODWAY"

  });


}
