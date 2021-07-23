package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WAClallamCountyParser extends SmartAddressParser {

  public WAClallamCountyParser() {
    super(CITY_LIST, "CLALLAM COUNTY", "WA");
    setFieldList("ADDR DATE TIME CODE UNIT APT PLACE X ID CALL INFO");
    removeWords("SQ");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us";
  }

  private static final Pattern MASTER = Pattern.compile("(?:(.*?) )?(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) (\\*New Call|911Hangup|[-A-Za-z]+) (?:([- A-Za-z0-9]+?) )? (?:(.*?) )?(\\d{4}-\\d{8})(?:  (.*))?");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern APT_PLACE_PTN = Pattern.compile("(?:#|APT|SUIT) *(\\S+) *(.*)");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\bDispatch received by unit \\S+\\b|\\bCall Number \\d+ was created from Call Number \\d+(?:\\([A-za-z0-9 :]*\\))?|(?:  |^)E911 Info.*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Incident")) return false;

    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      parseAddress(MSPACE_PTN.matcher(getOptGroup(match.group(1))).replaceAll(" "), data);
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      data.strCode = match.group(4);
      data.strUnit = getOptGroup(match.group(5));
      String place = getOptGroup(match.group(6));
      data.strCallId = match.group(7);
      body = getOptGroup(match.group(8));

      match = APT_PLACE_PTN.matcher(place);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        place = match.group(2);
      }

      if (isValidAddress(place)) {
        if (checkAddress(place) == STATUS_FULL_ADDRESS) {
          data.strCross = place;
        } else {
          data.strAddress = append(data.strAddress, " & ", place);
        }
      } else {
        data.strPlace = place;
      }

      // If an official address was not included, try finding one in the info block
      if (data.strAddress.isEmpty()) {
        parseAddress(StartType.START_OTHER, FLAG_NO_IMPLIED_APT, body, data);
      }

      for (String line : MSPACE_PTN.split(body)) {
        if (INFO_JUNK_PTN.matcher(line).matches()) continue;
        if (data.strCall.isEmpty()) {
          data.strCall = line;
        } else {
          data.strSupp = append(data.strSupp, "\n", line);
        }
      }

      if (data.strCall.isEmpty()) data.strCall = data.strCode;

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
