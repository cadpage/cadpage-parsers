package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXGalvestonCountyBParser extends DispatchOSSIParser {

  public TXGalvestonCountyBParser() {
    super(CITY_CODES, "GALVESTON COUNTY", "TX",
          "( CANCEL ADDR CITY! " +
          "| FYI ID SRC CALL ADDR CITY UNIT! UNIT/C+? ) INFO/N+");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BAYO", "BAYOU VISTA",
      "GACO", "GALVESTON COUNTY",
      "HITC", "HITCHCOCK",
      "LAMA", "LA MARQUE",
      "TC",   "TEXAS CITY",
      "TIKI", "TIKI ISLAND"
  });
}
