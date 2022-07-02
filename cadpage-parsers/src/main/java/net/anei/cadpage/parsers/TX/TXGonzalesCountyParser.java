package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXGonzalesCountyParser extends DispatchA72Parser {
  
  public TXGonzalesCountyParser() {
    super("GONZALES COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "gonzalessodispatch@gmail.com,@gvtc.com";
  }

}
