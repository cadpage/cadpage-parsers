package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class IADubuqueCountyParser extends DispatchOSSIParser {

  public IADubuqueCountyParser() {
    super("DUBUQUE COUNTY", "IA",
          "FYI? PRI CALL ADDR! X X INFO/N+");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }

}
