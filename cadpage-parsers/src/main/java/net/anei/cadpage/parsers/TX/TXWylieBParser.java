package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXWylieBParser extends DispatchA82Parser {

  public TXWylieBParser() {
    super("WYLIE", "TX");
  }

  @Override
  public String getFilter() {
    return "ispage@murphytx.org";
  }

}
