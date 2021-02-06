package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;


public class TXRoanokeParser extends DispatchA82Parser {

  public TXRoanokeParser() {
    super("ROANOKE", "TX");
  }

  @Override
  public String getFilter() {
    return "ICS_Messaging@roanokepolice.com";
  }
}
