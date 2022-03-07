package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.FieldProgramParser;

public class FLMartinCountyParser extends FieldProgramParser {

  public FLMartinCountyParser() {
    super("MARTIN COUNTY", "FL",
          "CALL:CALL! ADDR:ADDR! ID:ID! UNIT:UNIT! LAT:GPS1! LON:GPS2 END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
