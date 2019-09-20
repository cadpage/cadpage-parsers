package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;



public class LAWestBatonRougeParishParser extends DispatchA13Parser {
  
  public LAWestBatonRougeParishParser() {
    super(CITY_LIST, "WEST BATON ROUGE PARISH", "LA", A13_FLG_TRAIL_PLACE);
    setupMultiWordStreets("I-10 FRONTAGE");
    setupSpecialStreets(
        "AVENUE A",
        "AVENUE B",
        "AVENUE C",
        "AVENUE D",
        "AVENUE E",
        "AVENUE G",
        "BOULEVARD ACADIEN",
        "RUE DAUPHINE",
        "RUE GRAND PRE",
        "RUE ILE ST JEAN",
        "RUE NOUVELLE ECOSSE",
        "RUE ST PATRICK"
    );
  }
  
  @Override
  public String getFilter() {
    return "dispatch@wbrcouncil.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MARKER = Pattern.compile("[ A-Z]+:\\d{4}:[:0-9]+\n");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    Matcher match = MARKER.matcher(body);
    boolean good = match.lookingAt();
    if (!good) {
      body = "XXX:0000:00000\nREQ DISPATCH\n" + body;
    }
    if (!super.parseMsg(body, data)) return false;
    if (!good) {
      data.strSource = data.strCallId = "";
    }
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
    "ADDIS",
    "BRUSLY",
    "PORT ALLEN",
    "ANTONIO",
    "BELMONT",
    "BUECHE",
    "CHAMBERLIN",
    "DEVALLS",
    "ERWINVILLE",
    "ITHRA",
    "KAHNS",
    "LOBDELL",
    "LUKEVILLE"
  };
}
