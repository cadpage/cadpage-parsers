package net.anei.cadpage.parsers.IA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class IAStoryCountyParser extends DispatchOSSIParser {

  public IAStoryCountyParser() {
    super(CITY_CODES, "STORY COUNTY", "IA",
          "( CANCEL ADDR CITY! | FYI ADDR CITY? CALL! ) INFO+");
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
      "ELKH", "ELKHART",
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

      // Polk County
      "ALLE", "ALLEMAN",
      "POLK", "POLK CITY"
  });
}
