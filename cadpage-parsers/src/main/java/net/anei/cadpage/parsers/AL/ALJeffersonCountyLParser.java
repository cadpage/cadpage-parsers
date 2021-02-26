
package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Jefferson County, AL
 */
public class ALJeffersonCountyLParser extends DispatchH05Parser {

  public ALJeffersonCountyLParser() {
    super("JEFFERSON COUNTY", "AL",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "@HOMEWOODAL.ORG";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
