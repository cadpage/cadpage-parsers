package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXFriendswoodParser extends DispatchA82Parser {

  public TXFriendswoodParser() {
    super("FRIENDSWOOD", "TX");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "cadpaging@friendswood.com";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "606 DESERET DR",         "+29.514989,-95.199953"
  });

}
