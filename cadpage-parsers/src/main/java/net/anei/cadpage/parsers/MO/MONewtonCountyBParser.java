package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MONewtonCountyBParser extends DispatchOSSIParser {

  public MONewtonCountyBParser() {
    super(CITY_CODES, "NEWTON COUNTY", "MO",
          "( CANCEL ADDR! CITY? " +
          "| FYI ( ADDR/Z CITY APT? X+? ( GPS1 GPS2 | ) CALL! " +
                "| CALL PLACE ADDR/Z CITY! APT? X+?  ( GPS1 GPS2 | ) " +
                "| CALL ADDR/Z CITY! APT? X+?  ( GPS1 GPS2 | ) " +
                "| ADDR CITY? APT? X+? ( GPS1 GPS2 | ) CALL! " +
                "| CALL PLACE? ADDR! CITY X+?  ( GPS1 GPS2 | ) " +
                ") INFO/N+ " +
          ")");

    setupCities("NEWTON COUNTY");
    setupCityValues(CITY_CODES);

  }

  @Override
  public String getFilter() {
    return "CAD@nc-cdc.org,911@nc-cdc.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("=\n", " ");
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("APT")) return new AptField("(?:RM|ROOM|APT|LOT) *(.*)", true);
    if (name.equals("GPS1")) return new GPSField(1, "[-+]?\\d{2,3}\\.\\d*");
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      // if Same as following address field, disregard
      if (field.equals(getRelativeField(+1))) return;
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLIF", "CLIFF VILLAGE",
      "DEAC", "DENNIS ACRES",
      "DIAM", "DIAMOND",
      "FAIR", "FAIRVIEW",
      "GOOD", "GOODMAN",
      "GRAN", "GRANBY",
      "GRFS", "GREAT FALLS",
      "JOPL", "JOPLIN",
      "LEAW", "LEAWOOD",
      "LOMA", "LOMA LINDA",
      "NEOS", "NEOSHO",
      "NEWA", "NEWTONIA",
      "NEWT", "NEWTON COUNTY",
      "RACI", "RACINE",
      "REDI", "REDINGS MILL",
      "RITC", "RITCHEY",
      "SAGI", "SAGINAW",
      "SENE", "SENECA",
      "SHOA", "SHOAL CREEK DRIVE",
      "SHOE", "SHOAL CREEK ESTATES",
      "STAR", "STARK CITY",
      "STEL", "STELLA",
      "WENT", "WENTWORTH"
  });
}
