package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALChoctawCountyAParser extends FieldProgramParser {

  public ALChoctawCountyAParser() {
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

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern UNIT_BLK_PTN = Pattern.compile("[ -]+");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BLK_PTN.matcher(field).replaceAll("_");
      super.parse(field, data);
    }
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
      "BU", "BUTLER",
      "GT", "GILBERTOWN",
      "JA", "JACHIN",
      "LI", "LISMAN",
      "NE", "NEEDHAM",
      "PE", "PENNINGTON",
      "SI", "SILAS",
      "TX", "TOXEY",
      "WA", "WARD"

  });
}
