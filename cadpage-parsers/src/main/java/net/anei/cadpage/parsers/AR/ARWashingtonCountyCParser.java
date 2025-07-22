package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARWashingtonCountyCParser extends FieldProgramParser {

  public ARWashingtonCountyCParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "AR",
          "( RR_MARK/R ID CALL INFO " +
          "| ADDRCITY/S ( CALL_CHG PRI EMPTY EMPTY EMPTY EMPTY " +
                       "| ( UNIT | NAME ) PHONE EMPTY EMPTY EMPTY CALL PRI EMPTY? " +
                       ") ZA:MAP! geo:GPS! " +
          ") DISPATCHER! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("RR_MARK")) return new SkipField("CFS Closed:", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CALL_CHG")) return new CallField("CallType Changed to +(.*)", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+", true);
    if (name.equals("PRI"))  return new PriorityField("HIGH|MED|LOW|", true);
    if (name.equals("DISPATCHER")) return new SkipField("[A-Z]+", true);
    return super.getField(name);
  }

  private static final Pattern OOC_PTN = Pattern.compile("1 OUT OF COUNTY +(.*) +OUT OF COUNTY");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = OOC_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1), data);
        data.strCity = "OUT OF COUNTY";
      } else {
        super.parse(field, data);
      }
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "ELKINS",
      "ELM SPRINGS",
      "FARMINGTON",
      "FAYETTEVILLE",
      "GOSHEN",
      "GREENLAND",
      "JOHNSON",
      "LINCOLN",
      "PRAIRIE GROVE",
      "SPRINGDALE",
      "TONTITOWN",
      "WEST FORK",
      "WINSLOW",

      // Census-designated places
      "CANEHILL",
      "CINCINNATI",
      "EVANSVILLE",
      "MORROW",
      "SUMMERS",

      // Other unincorporated communities
      "APPLEBY",
      "ARNETT",
      "BALDWIN",
      "BANYARD",
      "BLACK OAK",
      "BLACKBURN",
      "BLUE SPRINGS VILLAGE",
      "BRENTWOOD",
      "CLYDE",
      "DURHAM",
      "DUTCH MILLS",
      "FAYETTE JUNCTION",
      "FLOSS",
      "GULLEY",
      "HABBERTON",
      "HARMON",
      "HARRIS",
      "HAZEL VALLEY",
      "HICKS",
      "HOGEYE",
      "HUBBARD",
      "MAYFIELD",
      "MCNAIR",
      "MOUNT OLIVE",
      "OAK GROVE",
      "ODELL",
      "ONDA",
      "PILGRIM'S REST",
      "PITKIN CORNER",
      "RHEA",
      "ROCHELLE RIVIERA",
      "SAVOY",
      "SHADY GROVE",
      "SKYLIGHT",
      "SONORA",
      "SPRING VALLEY",
      "STARKS",
      "STEELE",
      "STRAIN",
      "STRICKLER",
      "SULPHUR CITY",
      "SUNSET",
      "SUTTLE",
      "TOLU",
      "TUTTLE",
      "VINEY GROVE",
      "WALNUT GROVE",
      "WAR EAGLE COVE",
      "WEDDINGTON",
      "WEDINGTON WOODS",
      "WHEELER",
      "WHITE ROCK",
      "WOOLSEY",
      "WYMAN",
      "WYOLA"
  };
}
