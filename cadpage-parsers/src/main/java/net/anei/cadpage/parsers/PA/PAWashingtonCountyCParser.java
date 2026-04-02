package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class PAWashingtonCountyCParser extends DispatchProQAParser {

  public PAWashingtonCountyCParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "PA",
          "ID! CALL ( PLACE EMPTY/Z ADDR/Z CITY/Y! | ADDR APT CITY/Y ) UNIT? TIME Cross_Street:X? Lat:GPS1? Lon:GPS2 Description:INFO? INFO/N+", true);
    setupCities(CITY_CODES);
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String getFilter() {
    return "noreply@zollhosted.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("...", "");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]+");
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      return parse(field, data, false);
    }

    @Override
    public void parse(String field, Data data) {
      parse(field, data, true);
    }

    private boolean parse(String field, Data data, boolean force) {
      if (isLastField(0)) return false;
      if (field.isEmpty()) return true;
      int pt = field.indexOf('(');
      if (pt < 0) pt = field.indexOf("  ");
      if (pt >= 0) field = field.substring(0,pt).trim();
      if (force) {
        super.parse(field, data);
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "MONONGAHELA",
      "WASHINGTON",
      "WASHINGTON CITY",

      // Boroughs
      "ALLENPORT",
      "BEALLSVILLE",
      "BENTLEYVILLE",
      "BURGETTSTOWN",
      "CALIFORNIA",
      "CANONSBURG",
      "CENTERVILLE",
      "CHARLEROI",
      "CLAYSVILLE",
      "COAL CENTER",
      "COKEBURG",
      "DEEMSTON",
      "DONORA",
      "DUNLEVY",
      "EAST WASHINGTON",
      "ELCO",
      "ELLSWORTH",
      "FINLEYVILLE",
      "GREEN HILLS",
      "HOUSTON",
      "LONG BRANCH",
      "MARIANNA",
      "MCDONALD",
      "MIDWAY",
      "NEW EAGLE",
      "NORTH CHARLEROI",
      "ROSCOE",
      "SPEERS",
      "STOCKDALE",
      "TWILIGHT",
      "WEST BROWNSVILLE",
      "WEST MIDDLETOWN",

      // Townships
      "AMWELL TWP",
      "BLAINE TWP",
      "BUFFALO TWP",
      "CANTON TWP",
      "CARROLL TWP",
      "CECIL TWP",
      "CHARTIERS TWP",
      "CROSS CREEK TWP",
      "DONEGAL TWP",
      "EAST BETHLEHEM TWP",
      "EAST FINLEY TWP",
      "FALLOWFIELD TWP",
      "FRANKLIN TWP",
      "HANOVER TWP",
      "HOPEWELL TWP",
      "INDEPENDENCE TWP",
      "JEFFERSON TWP",
      "MORRIS TWP",
      "MOUNT PLEASANT TWP",
      "NORTH BETHLEHEM TWP",
      "NORTH FRANKLIN TWP",
      "NORTH STRABANE TWP",
      "NOTTINGHAM TWP",
      "PETERS TWP",
      "ROBINSON TWP",
      "SMITH TWP",
      "SOMERSET TWP",
      "SOUTH FRANKLIN TWP",
      "SOUTH STRABANE TWP",
      "UNION TWP",
      "WEST BETHLEHEM TWP",
      "WEST FINLEY TWP",
      "WEST PIKE RUN TWP",

      // Census-designated places
      "AARONSBURG",
      "ATLASBURG",
      "AVELLA",
      "BAIDLAND",
      "BULGER",
      "CECIL-BISHOP",
      "CROSS CREEK",
      "EIGHTY FOUR",
      "ELRAMA",
      "FREDERICKTOWN",
      "GASTONVILLE",
      "HENDERSONVILLE",
      "HICKORY",
      "JOFFRE",
      "LANGELOTH",
      "LAWRENCE",
      "MCGOVERN",
      "MCMURRAY",
      "MEADOWLANDS",
      "MILLSBORO",
      "MUSE",
      "PARIS",
      "SLOVAN",
      "SOUTHVIEW",
      "TAYLORSTOWN",
      "THOMPSONVILLE",
      "VAN VOORHIS",
      "WEST ALEXANDER",
      "WESTLAND",
      "WICKERHAM MANOR-FISHER",
      "WOLFDALE",
      "WYLANDVILLE",

      // Unincorporated communities
      "AMITY",
      "BLAINSBURG",
      "BURNSVILLE",
      "CONDIT CROSSING",
      "COOL VALLEY",
      "COURTNEY",
      "CRACKER JACK",
      "DAISYTOWN",
      "FALLOWFIELD",
      "FLORENCE",
      "FROGTOWN",
      "GAMBLES",
      "GLYDE",
      "GOOD INTENT",
      "HAZEL KIRK",
      "LABORATORY",
      "LOG PILE",
      "LOVER",
      "MANIFOLD",
      "MURDOCKSVILLE",
      "MCADAMS",
      "NORTH FREDERICKTOWN",
      "OLD CONCORD",
      "P AND W PATCH",
      "PROSPERITY",
      "RACCOON",
      "RICHEYVILLE",
      "SCENERY HILL",
      "STUDA",
      "VENETIA",
      "VESTABURG",
      "WOODROW"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CAN",      "CANTON TWP",
      "WASHIN",   "WASHINGTON"
  });
}
