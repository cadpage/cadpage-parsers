package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCCaldwellCountyParser extends DispatchOSSIParser {

  public NCCaldwellCountyParser() {
    super(CITY_CODES, "CALDWELL COUNTY", "NC",
           "( UNIT/Z ENROUTE ADDR CITY CALL! END " +
           "| CANCEL ADDR CITY APT? " +
           "| CALL ( ADDR/Z ID! " +
                  "| PLACE? ADDR/Z CITY! APT? X/Z+? ID " +
                  ") " +
           ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@caldwellcountync.org,7677";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("cadreports:")) {
      body = "CAD:" + body.substring(11);
    } else if (!body.startsWith("CAD:")) {
      body = "CAD:" + body;
    }
    if (body.contains(",Enroute,")) {
      return parseFields(stripFieldStart(body, "CAD:").split(","), data);
    } else {
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ENROUTE")) return new SkipField("Enroute", true);
    if (name.equals("CANCEL")) return new BaseCancelField("Clear Stand By");
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }

  private class MyAptField extends AptField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("APT ")) {
        super.parse(field.substring(4).trim(), data);
        return true;
      } else {
        return field.startsWith("DIST:");
      }
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BOOM", "BOOMER",
      "BR",   "BLOWING ROCK",
      "COLL", "COLLETTSVILLE",
      "GF",   "GRANITE FALLS",
      "GMW",  "GAMEWELL",
      "GC",   "GRACE CHAPEL",
      "HICK", "HICKORY",
      "HUD",  "HUDSON",
      "KC",   "KINGS CREEK",
      "LEN",  "LENOIR",
      "LR",   "LITTLE RIVER",
      "MORG", "MORGONTON",
      "NC",   "NORTH CATAWBA",
      "PATT", "PATTERSON",
      "RHOD", "RHODHISS",
      "SAW",  "SAWMILLS",
      "VAL",  "VALMEAD",
      "YAD",  "YADKIN VALLEY",
      "BETH", "BETHLEHEM"
  });

}
