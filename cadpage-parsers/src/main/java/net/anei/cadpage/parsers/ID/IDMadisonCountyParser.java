package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDMadisonCountyParser extends FieldProgramParser {

  public IDMadisonCountyParser() {
    super(CITY_LIST, "MADISON COUNTY", "ID",
          "CALL! ADDR APT? CITY? PLACE EMPTY Note:INFO INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("(?i:APT|LOT|RM|ROOM) *(.*)", true);
    return super.getField(name);
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
      "MONTEVIEW"
  };
}
