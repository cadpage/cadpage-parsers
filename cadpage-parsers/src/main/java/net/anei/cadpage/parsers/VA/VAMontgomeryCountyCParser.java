package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class VAMontgomeryCountyCParser extends DispatchH05Parser {

  VAMontgomeryCountyCParser() {
    super("MONTGOMERY COUNTY","VA", null);
    setProgram("RUN__REPORT%EMPTY! ADDR:ADDRCITY! CALL:CALL! PLACE:PLACE! ID:ID! INCIDENT_NUMBERS:ID! INCIDENT__TIMES:EMPTY! TIMES+? END_TIMES",
               FLDPROG_DOUBLE_UNDERSCORE);
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "@nrv911.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("END_TIMES")) return new SkipField("test|<END>|NOTICE:.*", true);
    return super.getField(name);
  }
}
