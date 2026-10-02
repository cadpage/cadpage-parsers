package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchC13Parser;

public class COElPasoCountyEParser extends DispatchC13Parser {

  public COElPasoCountyEParser() {
    super("EL PASO COUNTY", "CO");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
