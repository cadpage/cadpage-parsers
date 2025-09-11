package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchC03Parser;

public class TXVanAlstyneParser extends DispatchC03Parser {

  public TXVanAlstyneParser() {
    super("VAN ALSTYNE", "TX");
  }
  
  @Override
  public String getFilter() {
    return "cad_paging@vanalstynepolice.com";
  }
}
