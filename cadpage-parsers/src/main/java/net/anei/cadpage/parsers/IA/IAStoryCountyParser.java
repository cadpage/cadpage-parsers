package net.anei.cadpage.parsers.IA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class IAStoryCountyParser extends DispatchOSSIParser {

  public IAStoryCountyParser() {
    super(CITY_CODES, "STORY COUNTY", "IA",
          "( CANCEL ADDR! CITY? INFO/N+" +
          "| FYI ( ADDR/Z CITY/Y! CALL | CALL ADDR/Z CITY! |  CALL ADDR | ADDR CALL ) PHONE? DATETIME? INFO/N+? ID END" +
          "| CALL2 ( UNIT ADDR/Z CITY! | ADDR/Z CITY! | UNIT? ADDR ) Radio_Channel:CH? INFO/N+ END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "CAD@storycom.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("[A-Z]{4,5}", true);
    if (name.equals("PHONE")) return new PhoneField("\\d{10}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{9}", true);
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
