package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class IADesMoinesCountyBParser extends DispatchA74Parser {

  public IADesMoinesCountyBParser() {
    super("DES MOINES COUNTY", "IA");
  }

  @Override
  public String getFilter() {
    return "Dispatch@Descom911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
