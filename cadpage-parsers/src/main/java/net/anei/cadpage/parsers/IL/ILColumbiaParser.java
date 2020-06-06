package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class ILColumbiaParser extends DispatchBCParser {
  
  public ILColumbiaParser() {
    super("COLUMBIA", "IL");
  }
  
  @Override
  public String getFilter() {
    return "COLUMBIA@PUBLICSAFETYSOFTWARE.NET,COLUMBIA@OMNIGO.COM";
  }

}
