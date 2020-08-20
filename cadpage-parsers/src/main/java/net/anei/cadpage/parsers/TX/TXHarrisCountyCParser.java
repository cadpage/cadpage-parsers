package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXHarrisCountyCParser extends DispatchOSSIParser {

  public TXHarrisCountyCParser() {
    super(CITY_CODES, "HARRIS COUNTY", "TX",
          "( CANCEL ADDR! CITY X " +
          "| FYI ID SRC CALL ADDR CITY UNIT! ) INFO/N+");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}");
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,4}");
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "EL",  "EL LAGO",
      "LC",  "LEAGUE CITY",
      "NB",  "NASSAU BAY",
      "SB",  "SEABROOK"
  });

}
