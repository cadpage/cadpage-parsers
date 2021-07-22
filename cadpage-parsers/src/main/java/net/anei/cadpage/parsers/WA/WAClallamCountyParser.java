package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WAClallamCountyParser extends SmartAddressParser {

  public WAClallamCountyParser() {
    super(CITY_LIST, "CLALLAM COUNTY", "WA");
    setFieldList("ADDR APT DATE TIME UNIT SRC ID CALL INFO");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) ([ A-Z0-9]+)  (?:([A-Z]{3,5}) )?(\\d{4}-\\d{8})  (.*)");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\bDispatch received by unit \\S+\\b|\\bCall Number \\d+ was created from Call Number \\d+(?:\\([A-za-z0-9 :]*\\))?|(?:  |^)E911 Info.*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Incident")) return false;

    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      parseAddress(MSPACE_PTN.matcher(match.group(1).trim()).replaceAll(" "), data);
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      data.strUnit = match.group(4).trim();
      data.strSource = getOptGroup(match.group(5));
      data.strCallId = match.group(6);
      body = match.group(7).trim();
      for (String line : MSPACE_PTN.split(body)) {
        if (INFO_JUNK_PTN.matcher(line).matches()) continue;
        if (data.strCall.isEmpty()) {
          data.strCall = line;
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }
      return true;
    }

    return false;
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "FORKS",
    "PORT ANGELES",
    "SEQUIM",

    // Census-designated places
    "BELL HILL",
    "BLYN",
    "CARLSBORG",
    "CLALLAM BAY",
    "JAMESTOWN",
    "NEAH BAY",
    "PORT ANGELES EAST",
    "RIVER ROAD",
    "SEKIU",

    // Other communities
    "AGATE BEACH",
    "AGNEW",
    "BEAVER",
    "BOGACHIEL",
    "CRANE",
    "DIAMOND POINT",
    "DUNGENESS",
    "ELWHA",
    "FAIRHOLM",
    "HOKO",
    "JOYCE",
    "LA PUSH",
    "MAPLE GROVE",
    "MORA",
    "OZETTE",
    "PYSHT",
    "PIEDMONT",
    "SAPPHO",
    "SCHOOLHOUSE POINT",
    "SEKIU",
    "UPPER HOH"
  };

}
