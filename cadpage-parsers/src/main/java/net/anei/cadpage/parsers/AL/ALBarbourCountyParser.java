package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALBarbourCountyParser extends FieldProgramParser {

  public ALBarbourCountyParser() {
    super(CITY_LIST, "BARBOUR COUNTY", "AL",
          "ADDR/S PLACE TIME CALL! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    data.strSource = subject;

    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CLIO",
      "EUFAULA",

      // Towns
      "BAKERHILL",
      "BLUE SPRINGS",
      "CLAYTON",
      "LOUISVILLE",

      //Unincorporated communities
      "BATESVILLE",
      "ELAMVILLE",
      "SPRING HILL",
      "TEALS CROSSROADS",
      
      // Bullock County
      "MIDWAY"

  };

}
