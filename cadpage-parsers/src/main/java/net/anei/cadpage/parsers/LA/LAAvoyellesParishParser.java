package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class LAAvoyellesParishParser extends DispatchA46Parser {

  public LAAvoyellesParishParser() {
    super("AVOYELLES PARISH", "LA");
    }

  @Override
  public String getFilter() {
    return "beaufjuneau@yahoo.com,Avoyelles@911center.net";
  }
}
