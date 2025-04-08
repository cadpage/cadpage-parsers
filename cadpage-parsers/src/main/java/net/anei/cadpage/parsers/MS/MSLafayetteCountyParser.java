package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;



public class MSLafayetteCountyParser extends DispatchA74Parser {

  public MSLafayetteCountyParser() {
    super("LAFAYETTE COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "E911OMG@HOTMAIL.COM";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
