package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

/*
Acadian Ambulance, TX

*/

public class TXAcadianAmbulanceBParser extends DispatchA72Parser {

  public TXAcadianAmbulanceBParser() {
    super("", "TX");
  }
  
  @Override
  public String getLocName() {
    return "Acadian Ambulance, TX";
  }

  @Override
  public String getFilter() {
    return "bastropactive911@gmail.com,bastropactive911@co.bastrop.tx.us,TPS_Service@tylertech.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
}
