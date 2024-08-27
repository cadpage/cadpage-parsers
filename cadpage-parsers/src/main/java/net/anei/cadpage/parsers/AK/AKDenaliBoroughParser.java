package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class AKDenaliBoroughParser extends DispatchOSSIParser {

  public AKDenaliBoroughParser() {
    super("DENALI BOROUGH", "AK",
           "( CANCEL ADDR SKIP! INFO/N+ " +
           "| FYI UNIT? ADDR ( CALL! END | PLACE? CALL DATETIME! END ) )");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z]{4}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
