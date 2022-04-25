package net.anei.cadpage.parsers.ID;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDMadisonCountyParser extends FieldProgramParser {

  public IDMadisonCountyParser() {
    super(CITY_LIST, "MADISON COUNTY", "ID",
          "( ID CALL ( SELECT/NOCITY ADDR? " +
                    "| PLACE1? ADDR/Z! APT? City:CITY! Postal_Code:ZIP? " +
                    ") " +
          "| CALL! ADDR APT? CITY? PLACE EMPTY Note:INFO " +
          ") INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    setSelectValue(body.contains("\nCity:") ? "" : "NOCITY");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}");
    if (name.equals("PLACE1")) return new MyPlace1Field();
    if (name.equals("APT")) return new AptField("(?i:APT|LOT|RM|ROOM) *(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private Pattern APT_OR_CITY_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM)\\b|CITY:", Pattern.CASE_INSENSITIVE);
  private class MyPlace1Field extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (APT_OR_CITY_PTN.matcher(field).lookingAt()) return false;
      if (APT_OR_CITY_PTN.matcher(getRelativeField(+1)).lookingAt()) return false;
      if (!APT_OR_CITY_PTN.matcher(getRelativeField(+2)).lookingAt()) return false;
      parse(field, data);
      return true;
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *// *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = {
      // Adjacent counties
      "FREMONT COUNTY",
      "TETON COUNTY",
      "BONNEVILLE COUNTY",
      "JEFFERSON COUNTY",

      // County
      "MADISON",

      // Cities
      "NEWDALE",
      "REXTON",
      "REXBURG",
      "SUGAR CITY",
      "THORNTON",
      "ST ANTHONY",
      "RIGBY",
      "LEWISVILLE",
      "RIRIE",
      "MENAN",
      "TERRETON",
      "HAMER",
      "MONTEVIEW",

      "OUT OF COUNTY"
  };
}
