package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class MSDeSotoCountyCParser extends DispatchA74Parser {

  public MSDeSotoCountyCParser() {
    super("DESOTO COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "dispatch@MarshallMS911.info";
  }


  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
