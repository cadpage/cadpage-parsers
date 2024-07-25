package net.anei.cadpage.parsers.IA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class IAStoryCountyParser extends DispatchOSSIParser {

  public IAStoryCountyParser() {
    super(CITY_CODES, "STORY COUNTY", "IA",
          "( CANCEL ADDR CITY! INFO/N+" +
          "| FYI UNIT1/C+? ( ADDR/Z CITY! CALL | CALL ADDR/Z CITY! |  CALL ADDR | ADDR CALL ) INFO/N+? ID END" +
          "| UNIT2 CALL PRI ADDR CITY! INFO/N+ " +
          "| ADDR? INFO/N+? DATETIME! UNIT1! CITY! END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "CAD@storycom.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) {
      if (subject.length() > 0  || body.startsWith("CANCEL;")) {
        body = stripFieldStart(body, "CAD:");
        body = "CAD:" + append(subject, ": ", body);
      }
    }
    return super.parseMsg(body, data);
  }

  private static final String UNIT_PTN_STR = "(?:[A-Z]{3}[A-Z0-9])";
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT1")) return new UnitField(UNIT_PTN_STR, true);
    if (name.equals("UNIT2")) return new UnitField("(?:\\b" + UNIT_PTN_STR + "\\b,?)+", true);
    if (name.equals("PRI")) return new PriorityField("F\\d", true);
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  @Override
  public String adjustMapCity(String city){
    if (city.equalsIgnoreCase("STANHOPE")) return "STORY CITY";
    return city;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AMES", "AMES",
      "CAMB", "CAMBRIDGE",
      "COLL", "COLLINS",
      "COLO", "COLO",
      "GILB", "GILBERT",
      "HUXL", "HUXLEY",
      "KELL", "KELLY",
      "MAX",  "MAXWELL",
      "MAXW", "MAXWELL",
      "MCCA", "MCCALLSBURG",
      "MING", "MINGO",
      "NEVA", "NEVADA",
      "RHOD", "RHODES",
      "ROLA", "ROLAND",
      "SHEL", "SHELDAHL",
      "SLAT", "SLATER",
      "STOR", "STORY CITY",
      "ZEAR", "ZEARING",

      // Boone County
      "BOON", "BOONE",
      "LUTH", "LUTHER",
      "MADR", "MADRID",

      // Hamilton County
      "RAND", "RANDALL",
      "STAN", "STANHOPE",

      // Jasper County
      "MING", "MINGO",

      // Marshall County
      "SAIN", "ST ANTHONY",
      "STAT", "STATE CENTER",

      // Polk County
      "ALLE", "ALLEMAN",
      "ELKH", "ELKHART",
      "POLK", "POLK CITY"
  });
}
