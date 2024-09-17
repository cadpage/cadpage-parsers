package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class PANorthamptonCountyDParser extends DispatchProQAParser {

  public PANorthamptonCountyDParser() {
    super("NORTHAMPTON COUNTY", "PA",
          "ID! UNIT PRI TIME SKIP CALL ADDR APT CITY! INFO/N+", true);
  }

  @Override
  public String getFilter() {
    return "noreply@zollhosted.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("Emergency|Routine|Pre-Scheduled", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
