package net.anei.cadpage.parsers.AL;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALChoctawCountyParser extends FieldProgramParser {

  public ALChoctawCountyParser() {
    super(CITY_CODES, "CHOCTAW COUNTY", "AL",
          "UNIT CALL ADDR/S INFO GPS! END");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "choctawcountye911@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Choctaw E911")) return false;
    if (!parseFields(body.split("//", -1), data)) return false;
    data.strAddress = stripFieldEnd(data.strAddress, " - Sector");
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "BUTLER",
      "GILBERTOWN",
      "LISMAN",
      "NEEDHAM",
      "PENNINGTON",
      "SILAS",
      "TOXEY",

      // Census-designated place
      "CULLOMBURG",

      // Unincorporated communities
      "BARRYTOWN",
      "BLADON SPRINGS",
      "CROMWELL",
      "EDNA",
      "JACHIN",
      "MELVIN",
      "MOUNT STERLING",
      "PUSHMATAHA",
      "RIDERWOOD",
      "ROBJOHN",
      "SPRING HILL",
      "YANTLEY"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "GT",  "GILBERTOWN",
      "TX",  "TOXEY"
  });
}
