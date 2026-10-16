package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class TXEctorCountyParser extends DispatchA27Parser {
  
  public TXEctorCountyParser() {
    super("ECTOR COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "noreply_ciscad@odessa-tx.gov";
  }

}
