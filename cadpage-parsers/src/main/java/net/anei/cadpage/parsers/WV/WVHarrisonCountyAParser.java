package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Harrison County, WV
 */
public class WVHarrisonCountyAParser extends FieldProgramParser {

  public WVHarrisonCountyAParser() {
    super("HARRISON COUNTY", "WV",
           "ID ADDR EMPTY EMPTY CITY X1 X! Map:MAP! EMPTY CALL EMPTY EMPTY EMPTY EMPTY ESN:BOX!");
  }

  @Override
  public String getFilter() {
    return "911@harrco911.org,harrison@911page.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X1")) return new CrossField("XStr- *(.*)",   true);
    return super.getField(name);
  }
}
