package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXBrazoriaCountyEParser extends DispatchOSSIParser {

  public TXBrazoriaCountyEParser() {
    super(CITY_CODES, "BRAZORIA COUNTY", "TX",
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? CALL ADDR! CITY? X+? ( PHONE | NAME PHONE ) END )");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PHONE")) return new PhoneField("\\d{10}", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "DANB",   "DANBURY",
      "IC",     "IOWA COLONY",
      "MANV",   "MANVEL",
      "ROSH",   "ROSHARON"
  });
}
