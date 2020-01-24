package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MSDeSotoCountyBParser extends DispatchA48Parser {

  public MSDeSotoCountyBParser() {
    super(MSDeSotoCountyAParser.CITY_LIST, "DESOTO COUNTY", "MS", FieldType.X, A48_NO_CODE);
  }
}
