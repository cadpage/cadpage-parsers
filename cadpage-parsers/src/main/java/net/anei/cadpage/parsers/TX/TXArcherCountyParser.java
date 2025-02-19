package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TXArcherCountyParser extends DispatchA71Parser {

  public TXArcherCountyParser() {
    this("ARCHER COUNTY", "TX");
  }

  public TXArcherCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "cadalerts@kologik.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
