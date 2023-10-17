package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ARSpringdaleParser extends DispatchH05Parser {

  public ARSpringdaleParser() {
    super("BENTON COUNTY", "AR", 
          "CALL:CALL! PLACE:PLACE! ADDRESS:ADDRCITY! CROSS_STREETS:X! MAP:MAP! UNIT:UNIT! ID:ID! NARRATIVE:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "cadpaging@springdalear.gov";
  }
}
