package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class ORDouglasCountyDParser extends DispatchH06Parser {

  public ORDouglasCountyDParser() {
    super("DOUGLAS COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "dccad-NoReply2@douglascountyor.gov";
  }

}
