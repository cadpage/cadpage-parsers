package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class WVMcDowellCountyBParser extends DispatchA48Parser {

  public WVMcDowellCountyBParser() {
    super(CITY_LIST, "MCDOWELL COUNTY", "WV", FieldType.NONE, A48_ONE_WORD_CODE);
  }

  @Override
  public String getFilter() {
    return "mccad@frontier.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nNextGen");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

  private static final String[] CITY_LIST = new String[] {
      // Cities
      "GARY",
      "KEYSTONE",
      "WAR",
      "WELCH",

      // Towns
      "ANAWALT",
      "BRADSHAW",
      "DAVY",
      "IAEGER",
      "KIMBALL",
      "NORTHFORK",

      // Magisterial districts
      "BIG CREEK",
      "BROWNS CREEK",
      "NORTH ELKIN",
      "SANDY RIVER",

      // Census-designated places
      "BARTLEY",
      "BERWIND",
      "BIG SANDY",
      "CRUMPLER",
      "CUCUMBER",
      "MAYBEURY",
      "PAGETON",
      "RAYSAL",
      "RODERFIELD",
      "VIVIAN",

      // Unincorporated communities
      "ALGOMA",
      "APPLE GROVE",
      "ASCO",
      "ASHLAND",
      "ATWELL",
      "AVONDALE",
      "BEARTOWN",
      "BIG FOUR",
      "BISHOP",
      "BLACK WOLF",
      "BOTTOM CREEK",
      "CANEBRAKE",
      "CAPELS",
      "CARETTA",
      "CARLOS",
      "CARSWELL",
      "COALWOOD",
      "ECKMAN",
      "ELBERT",
      "ELKHORN",
      "ENGLISH",
      "ENNIS",
      "ERIN",
      "FARADAY",
      "FILBERT",
      "GILLIAM",
      "HAVACO",
      "HEMPHILL",
      "HENSLEY",
      "HULL",
      "ISABAN",
      "JACOBS FORK",
      "JED",
      "JENKINJONES",
      "JOHNNYCAKE",
      "JOLO",
      "KYLE",
      "LANDGRAFF",
      "LECKIE",
      "LEX",
      "LILA",
      "LITWAR",
      "MAITLAND",
      "MARINE",
      "MCDOWELL",
      "MOHAWK",
      "MOHEGAN",
      "MONSON",
      "NEWHALL",
      "PANTHER",
      "PAYNESVILLE",
      "POWHATAN",
      "PREMIER",
      "REAM",
      "RIFT",
      "ROCKRIDGE",
      "ROLFE",
      "SANDY HUFF",
      "SIX",
      "SKYGUSTY",
      "SQUIRE",
      "SUPERIOR",
      "SWITCHBACK",
      "THORPE",
      "TWIN BRANCH",
      "UNION CITY",
      "UPLAND",
      "VALLSCREEK",
      "VENUS",
      "WARRIORMINE",
      "WILCOE",
      "WORTH",
      "YERBA",
      "YUKON"
  };

}
