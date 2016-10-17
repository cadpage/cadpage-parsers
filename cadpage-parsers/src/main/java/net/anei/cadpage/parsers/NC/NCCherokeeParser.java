package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class NCCherokeeParser extends DispatchB2Parser {

  public NCCherokeeParser() {
    super("CHEROKEE 911:", CITY_LIST, "CHEROKEE", "NC");
    setupMultiWordStreets("CHEROKEE BOYS CLUB");
    removeWords("ARCH");
  }
  
  @Override
  public String getFilter() {
    return "CHEROKEE 911@mydomain.com,Cherokee@mydomain.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    String city = PLACE_TABLE.getProperty(data.strCity);
    if (city != null) {
      data.strPlace = append(data.strPlace, " - ", data.strCity);
      data.strCity = city;
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "PLACE CITY");
  }

  private static final String[] CITY_LIST = new String[]{
    "BIGWITCH",
    "BIG COVE",
    "BIRDTOWN",
    "CHEROKEE",
    "PAINTTOWN",
    "PAINT TOWN",
    "WHITTIER",
    "WOLFETOWN",   
    "YELLOWHILL"
  };
  
  private static final Properties PLACE_TABLE = buildCodeTable(new String[]{
      "BIG COVE",      "CHEROKEE",
      "YELLOWHILL",    "CHEROKEE",
      "BIRDTOWN",      "CHEROKEE",
      "PAINT TOWN",    "WITTIER",
      "PAINTTOWN",     "WITTIER",
      "WOLFETOWN",     "CHEROKEE"
  });
  
}