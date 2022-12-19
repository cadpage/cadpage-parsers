package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Edina, MN
 */
public class MNEdinaParser extends DispatchOSSIParser {
  public MNEdinaParser() {
    super(CITY_CODES, "EDINA", "MN",
        "( CANCEL ADDR CITY | FYI? CALL ADDR CITY CODE X/Z+? ID ID/L? ( UNIT/Z SRC! | SRC! | UNIT ) ) INFO/N+");
  }

  public String getFilter() {
    return "CAD@ci.edina.mn.us";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL"))  return new BaseCancelField("[A-Z ]+ SELECT CALLBACK");
    if (name.equals("CODE")) return new CodeField("[A-Z]+|911|AOAK9", true);
    if (name.equals("ID")) return new IdField("\\d{7,10}");
    if (name.equals("UNIT")) return new UnitField("[A-Z]*\\d+(?:,[A-Z]*\\d+)*|\\d{3}", false);
    if (name.equals("SRC")) return new SourceField("[A-Z]+\\d+", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "EDI",            "EDINA",
      "RCH",            "RICHFIELD",
      "GV",             "GOLDEN VALLEY",
      "BLO",            "BLOOMINGTON",
      "MPLS",           "MINNEAPOLIS",
      "BROOKLYN PARK",  "BROOKLYN PARK",
      "SLP",            "ST LOUIS PARK"
  });

}
