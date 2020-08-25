package net.anei.cadpage.parsers.MD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MDKentCountyCParser extends DispatchA19Parser {

  public MDKentCountyCParser() {
    super(CITY_CODES, "KENT COUNTY", "MD");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,911@Kentgov.org,@alert.active911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.length() > 0) {
      data.strState = (DE_CITY_SET.contains(data.strCity) ? "DE" : "MD");
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BET", "BETTERTON",
      "CC",  "CHESAPEAKE CITY",
      "CEC", "CECILTON",
      "CEN", "CENTREVILLE",
      "CH",  "CHURCH HILL",
      "CHE", "CHESTERTOWN",
      "CHR", "CHARLESTOWN",
      "CHS", "CHESTER",
      "CHW", "CHESWOLD",
      "CLA", "CLAYTON",
      "CRU", "CRUMPTON",
      "DOV", "DOVER",
      "ELK", "ELKTON",
      "GAL", "GALENA",
      "GRA", "GRASONVILLE",
      "HAR", "HARTLEY",
      "HP",  "HACKS POINT",
      "KCO", "KENT COUNTY",
      "KEN", "KENNEDYVILLE",
      "KI",  "KENT ISLAND",
      "LC",  "LITTLE CREEK",
      "LEI", "LEIPSIC",
      "MID", "MIDDLETOWN",
      "MIL", "MILLINGTON",
      "NE",  "NORTH EAST",
      "ODE", "ODESSA",
      "PD",  "PORT DEPOSIT",
      "PER", "PERRYVILLE",
      "QUE", "QUEENSTOWN",
      "RH",  "ROCK HALL",
      "RS",  "RISING SUN",
      "SMY", "SMYRNA",
      "STE", "STEVENSVILLE",
      "SUD", "SUDLERSVILLE",
      "TOW", "TOWNSEND",
      "WOR", "WORTON"
  });

  private static final Set<String> DE_CITY_SET = new HashSet<String>(Arrays.asList(
      "CHESWOLD",
      "CLAYTON",
      "DOVER",
      "HARTLEY",
      "LITTLE CREEK",
      "LEIPSIC",
      "MIDDLETOWN",
      "ODESSA",
      "SMYRNA",
      "TOWNSEND"
  ));

}
