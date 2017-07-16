package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class MSPearlRiverCountyParser extends DispatchA46Parser {

  public MSPearlRiverCountyParser() {
    super("PEARL RIVER COUNTY", "MS");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@PicayunePolice.com,picayunepd@pagingpts.com";
  }
}
