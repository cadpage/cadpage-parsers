package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

/**
 * Wyoming County, WV
 */
public class WVWyomingCountyAParser extends DispatchB2Parser {
  
  private static final Pattern ID_PTN = Pattern.compile("^(\\d+):");


  public WVWyomingCountyAParser() {
    super("911-CENTER:", CITY_LIST, "WYOMING COUNTY", "WV");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = ID_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end()).trim();
    return super.parseMsg(body,  data);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    "ALLEN JUNCTION",
    "ALPOCA",
    "AMIGO",
    "BAILEYSVILLE",
    "BEECHWOOD",
    "BLACK EAGLE",
    "BRENTON",
    "BUD",
    "CLEAR FORK",
    "COAL MOUNTAIN",
    "CORINNE",
    "COVEL",
    "CYCLONE",
    "FANNY",
    "FANROCK",
    "GARWOOD",
    "GLEN FORK",
    "GLEN ROGERS",
    "GLOVER",
    "HANOVER",
    "HERNDON",
    "IKES FORK",
    "ITMANN",
    "JESSE",
    "KEY ROCK",
    "KOPPERSTON",
    "LYNCO",
    "MABEN",
    "MARIANNA",
    "MATHENY",
    "MATOAKA",
    "MCGRAWS-TIPPLE",
    "MILAM",
    "MULLENS",
    "NEW RICHMOND",
    "NORTH SPRING",
    "OCEANA",
    "OTSEGO",
    "PIERPONT",
    "PINEVILLE",
    "RAVENCLIFF",
    "ROCK VIEW",
    "SABINE",
    "SAULSVILLE",
    "SIMON",
    "STEPHENSON",
    "TRALEE",
    "WINDOM",
    "WOLF PEN",
    "WYCO",
    "WYOMING"
  };
}
