package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class OHMahoningCountyBParser extends DispatchProQAParser {

  public OHMahoningCountyBParser() {
    super("MAHONING COUNTY", "OH",
          "ID! TIME CALL CALL/L ADDR APT CITY CALL/SDS PROQA_DET! INFO/N+", true);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("PROQA_DET")) return new SkipField("<PROQA_DET>");
    return super.getField(name);
  }

}
