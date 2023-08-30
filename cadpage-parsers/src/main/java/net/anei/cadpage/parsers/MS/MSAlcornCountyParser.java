package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class MSAlcornCountyParser extends DispatchA72Parser {
  
  public MSAlcornCountyParser() {
    super("ALCORN COUNTY", "MS");
  }
  
  @Override
  public String getFilter() {
    return "alcorn911@co.alcorn.ms.us";
  }

}
