package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;

public class NCDurhamCountyBParser extends FieldProgramParser {

  public NCDurhamCountyBParser() {
    super("DURHAM COUNTY", "NC",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! LATITUDE:GPS1! LONGITUDE:GPS2! ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! END");
  }

  @Override
  public String getFilter() {
    return "GSOC@wolfspeed.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
