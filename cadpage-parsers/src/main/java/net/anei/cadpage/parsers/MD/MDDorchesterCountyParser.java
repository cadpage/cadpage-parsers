package net.anei.cadpage.parsers.MD;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDDorchesterCountyParser extends GroupBestParser {

  public MDDorchesterCountyParser() {
    super(new MDDorchesterCountyAParser(),
           new MDDorchesterCountyBParser());
  }

  static final String[] MAP_ADJ_TABLE = new String[] {
      "BLTW",                     "BELTWAY",
      "BY-P",                     "BYPASS",
      "DORCHESTER SQUARE MALL",   "DORCHESTER SQUARE",
      "EAST NEW MARKET HURL",     "EAST NEW MARKET HURLOCK",
      "EAST NEW MARKET ELLW",     "EAST NEW MARKET ELWOOD",
      "EAST NEW MARKET RHOD",     "EAST NEW MARKET RHODESDALE"
  };

  static final Properties CITY_CODES =
    buildCodeTable(new String[]{
        "BISH", "BISHOPS HEAD",
        "CAM",  "CAMBRIDGE",
        "CAMB", "CAMBRIDGE",
        "CHU",  "CHURCH CREEK",
        "CHUR", "CHURCH CREEK",
        "CRA",  "CRAPO",
        "CRO",  "CROCHERON",
        "DCO",  "DORCHESTER COUNTY",
        "DSO",  "DORCHESTER COUNTY OTHER",
        "EAST", "EAST NEW MARKET",
        "ENM",  "EAST NEW MARKET",
        "FED",  "FEDERALSBURG",
        "FEDE", "FEDERALSBURG",
        "FSH",  "FISHING CREEK",
        "HUR",  "HURLOCK",
        "HURL", "HURLOCK",
        "LINK", "LINKWOOD",
        "LNK",  "LINKWOOD",
        "MADI", "MADISON",
        "PRS",  "PRESTON",
        "PRES", "PRESTON",
        "RHO",  "RHODESDALE",
        "RHOD", "RHODESDALE",
        "SEAF", "RHODESDALE",    // ????
        "SEC",  "SECRETARY",
        "SECR", "SECRETARY",
        "TOD",  "TODDVILLE",
        "TODD", "TODDVILLE",
        "TRP",  "TRAPPE",
        "TYL",  "TAYLORS ISLAND",
        "TAYL", "TAYLORS ISLAND",
        "VEN",  "VIENNA",
        "VIEN", "VIENNA",
        "WIN",  "WINGATE",
        "WOL",  "WOOLFORD",
        "WOOL", "WOOLFORD"
    });

}
