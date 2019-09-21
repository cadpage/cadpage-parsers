package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;

public class TXBurlesonCountyParser extends DispatchA18Parser {
  
  public TXBurlesonCountyParser() {
    super(CITY_LIST, "BURLESON COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "calwellactive911@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    body = subject + '\n' + body;
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "CALDWELL",
      "SNOOK",
      "SOMERVILLE",

      // Unincorporated communities
      "BIRCH",
      "BLACK JACK",
      "CHRIESMAN",
      "CLAY",
      "COOKS POINT",
      "DAVIDSON",
      "DEANVILLE",
      "FRENSTAT",
      "GOODWILL",
      "GUS",
      "HARMONY",
      "HIX",
      "HOGG",
      "LYONS",
      "MERLE",
      "NEW TABOR",
      "RITA",
      "SCOFIELD",
      "TUNIS",
      "WILCOX",

      // Bygone communities
      "PITTSBRIDGE"

  };

}
