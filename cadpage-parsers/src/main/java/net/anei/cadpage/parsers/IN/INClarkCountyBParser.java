package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class INClarkCountyBParser extends DispatchH05Parser {

  public INClarkCountyBParser() {
    super("CLARK COUNTY", "IN",
          "DATETIME MAP ADDRCITY PLACE X CALL! INFO+BLK+? UNIT! TIMES");
  }

  @Override
  public String getFilter() {
    return "alert@clarkcounty911.com";
  }

  @Override
  public Field getField(String name) {
    return super.getField(name);
  }
}
