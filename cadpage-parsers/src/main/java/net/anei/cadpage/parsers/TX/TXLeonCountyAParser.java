package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Leon County, TX
 */
public class TXLeonCountyAParser extends FieldProgramParser{
  
  public TXLeonCountyAParser() {
    super("LEON COUNTY", "TX",
        "CALL PLACE ADDR APT CITY! Note:INFO+");
  }

  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com ";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    return parseFields(body.split("\n"), 5, data);
  }
  @Override
  protected Field getField(String name) {
  if (name.equals("APT")) return new AptField("Apt *(.*)", true);
  return super.getField(name);
  }
}