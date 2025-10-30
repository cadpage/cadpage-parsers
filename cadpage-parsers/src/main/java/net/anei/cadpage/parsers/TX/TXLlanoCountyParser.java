package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXLlanoCountyParser extends DispatchA72Parser {
  
  public TXLlanoCountyParser() {
    super("LLANO COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "llanomsoffice@co.llano.tx.us";
  }

}
