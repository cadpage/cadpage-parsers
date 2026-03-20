package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class WVSummitBechtelReserveAParser extends DispatchA24Parser {
  
  public WVSummitBechtelReserveAParser() {
    super("SUMMIT BECHTEL RESERVE", "WV");
  }
  
  @Override
  public String getFilter() {
    return "cad@mail.incidentcad.com,cad@mail.incidentcad.dev";
  }

}
