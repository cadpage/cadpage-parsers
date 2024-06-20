package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;


public class CTNewHavenCountyAParser extends DispatchRedAlertParser {

  private static final Pattern BAD_MSG_PTN = Pattern.compile("\\d{10} .*");
  private static final Pattern MISSING_DOTS = Pattern.compile("(.*[^.]) (\\d\\d:\\d\\d)\\b *(.*)");
  private static final Pattern CITY_ZIP_PTN = Pattern.compile("(.*) \\d{5}");
  private static final Pattern PRI_CALL_PTN = Pattern.compile("CODE (\\d), *- *(.*)");

  public CTNewHavenCountyAParser() {
    super(CITY_LIST, "NEW HAVEN COUNTY", "CT");
    setupMultiWordStreets(
        "BLOCK ISLAND",
        "BOSTON POST",
        "CHESTNUT GROVE",
        "LEDGE HILL",
        "LONG HILL",
        "THISTLE ROCK"
    );
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // Eliminate CTNewHavenCountyE messages
    if (body.startsWith("COMPLAINT TYPE:")) return false;

    // Eliminate CTNewHavenCountyA messages
    if (BAD_MSG_PTN.matcher(body).matches()) return false;

    // This is mostly a Red Alert format with some variations we have to correct for
    String info = "";
    Matcher match = MISSING_DOTS.matcher(body);
    if (match.matches()) {
      body = match.group(1) + " . . " + match.group(2);
      info = match.group(3).trim();
    }

    if (!super.parseMsg(subject, body, data)) return false;

    match = PRI_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strPriority = match.group(1);
      data.strCall = match.group(2);
    }

    match = CITY_ZIP_PTN.matcher(data.strCity);
    if (match.matches()) {
      data.strCity = match.group(1).trim();
    } else {
      String addr = data.strAddress;
      data.strSource = data.strCity;
      data.strAddress = data.strCity = "";
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
      if (data.strCity.length() == 0) data.strCity = data.strSource;
    }

    data.strSupp = append(data.strSupp, " / ", info);

    return true;
  }

  @Override
  public String getProgram() {
    return "PRI " + super.getProgram().replace("CITY", "CITY SRC") + " INFO";
  }

  private static final String[] CITY_LIST = new String[]{
    "ALLENPORT",
    "AMWELL TWP",
    "BEALLSVILLE",
    "BENTLEYVILLE",
    "BLAINE TWP",
    "BUFFALO TWP",
    "BURGETTSTWP",
    "CALIFORNIA",
    "CANONSBURG",
    "CANTON TWP",
    "CARROLL TWP",
    "CECIL TWP",
    "CENTERVILLE",
    "CHARTIERS TWP",
    "CLAYSVILLE",
    "COAL CENTER",
    "COKEBURG",
    "CHARLEROI",
    "CROSS CREEK TWP",
    "DEEMSTON",
    "DONEGAL TWP",
    "DONORA",
    "DUNLEVY",
    "EAST BETHLEHEM TWP",
    "EAST FINLEY TWP",
    "ELCO",
    "ELLSWORTH",
    "EAST WASHINGTON",
    "FALLOWFIELD TWP",
    "FINLEYVILLE",
    "GREEN HILLS",
    "HANOVER TWP",
    "HOPEWELL TWP",
    "HOUSTON",
    "INDEPENDENCE TWP",
    "JEFFERSON TWP",
    "LONG BRANCH",
    "MARIANNA",
    "MCDONALD",
    "MIDWAY",
    "MONONGAHELA CITY",
    "MORRIS TWP",
    "MOUNT PLEASANT TWP",
    "NORTH BETHLEHEM TWP",
    "NORTH CHARLEROI",
    "ANSONIA",
    "BEACON FALLS TWP",
    "BETHANY TWP",
    "BRANFORD TWP",
    "CHESHIRE TWP",
    "DERBY",
    "EAST HAVEN TWP",
    "GUILFORD TWP",
    "HAMDEN TWP",
    "AUGERVILLE",
    "CENTERVILLE TWP",
    "DUNBAR HILL",
    "HAMDEN PLAINS",
    "HIGHWOOD",
    "MIX DISTRICT",
    "MOUNT CARMEL",
    "SPRING GLEN",
    "WEST WOODS",
    "HAMDEN HILLS",
    "WHITNEYVILLE",
    "MADISON TWP",
    "MERIDEN",
    "MIDDLEBURY TWP",
    "MILFORD",
    "DEVON",
    "WOODMONT",
    "NAUGATUCK",
    "NEW HAVEN",
    "AMITY",
    "CEDAR HILL",
    "CITY POINT",
    "DOWNTWP",
    "EAST ROCK",
    "FAIR HAVEN",
    "FAIR HAVEN HEIGHTS",
    "LONG WHARF",
    "MILL RIVER",
    "QUINNIPIAC MEADOWS",
    "WESTVILLE",
    "WOOSTER SQUARE",
    "NORTH BRANFORD TWP",
    "NORTHFORD",
    "NORTH HAVEN TWP",
    "ORANGE TWP",
    "OXFORD TWP",
    "PROSPECT TWP",
    "SEYMOUR TWP",
    "SOUTHBURY TWP",
    "WALLINGFORD TWP",
    "YALESVILLE",
    "WATERBURY",
    "WEST HAVEN",
    "WOLCOTT TWP",
    "WOODBRIDGE TWP",
    "GUILFORD",
    "BRANFORD"
  };
}
