package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXFriendswoodParser extends DispatchA82Parser {

  public TXFriendswoodParser() {
    super("FRIENDSWOOD", "TX");
  }

  @Override
  public String getFilter() {
    return "cadpaging@friendswood.com";
  }


}
