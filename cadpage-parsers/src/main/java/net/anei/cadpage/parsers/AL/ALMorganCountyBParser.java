package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALMorganCountyBParser extends SmartAddressParser {

  public ALMorganCountyBParser() {
    super(CITY_LIST, "MORGAN COUNTY", "AL");
    setFieldList("CALL ADDR APT CITY SRC CH INFO");
  }

  @Override
  public String getFilter() {
    return "cad-no-reply@morgan911.org";
  }

  private static final Pattern MASTER = Pattern.compile("(.+?) ([A-Z][a-z][ A-Za-z]*(?: V?FD)?) - (TAC\\d+)\\b(.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^|; *)\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    data.strCall = subject;

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(1).trim(), data);
    data.strSource = match.group(2);
    data.strChannel = match.group(3);
    data.strSupp = INFO_BRK_PTN.matcher(match.group(4).trim()).replaceAll("\n").trim();

    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "FALKVILLE",
      "BERKELEY SPRINGS",
      "PAW PAW",

      // Magisterial districts
      "ALLEN",
      "BATH",
      "CACAPON",
      "ROCK GAP",
      "SLEEPY CREEK",
      "TIMBER RIDGE",

      // Census-designated places
      "GREAT CACAPON",

      // Unincorporated communities
      "BERRYVILLE",
      "BURNT FACTORY",
      "CAMPBELLS",
      "CHERRY RUN",
      "DOE GULLY",
      "DUCKWALL",
      "GREEN RIDGE",
      "GREENWOOD",
      "HANCOCK",
      "HANSROTE",
      "HOLTON",
      "JEROME",
      "JIMTOWN",
      "JOHNSONS MILL",
      "LARGENT",
      "LINEBURG",
      "MAGNOLIA",
      "MOUNT TRIMBLE",
      "NEW HOPE",
      "NORTH BERKELEY",
      "OAKLAND",
      "OMPS",
      "ORLEANS CROSS ROADS",
      "REDROCK CROSSING",
      "RIDERSVILLE",
      "RIDGE",
      "ROCK GAP",
      "SIR JOHNS RUN",
      "SLEEPY CREEK",
      "SMITH CROSSROADS",
      "SPOHRS CROSSROADS",
      "STOTLERS CROSSROADS",
      "UNGER",
      "WOODMONT",
      "WOODROW"

  };

}
