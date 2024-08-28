package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MNMeekerCountyParser extends SmartAddressParser {

  public MNMeekerCountyParser() {
    super(CITY_LIST, "MEEKER COUNTY", "MN");
    setFieldList("ADDR APT CITY ST PLACE GPS CALL INFO");
  }

  @Override
  public String getFilter() {
    return "zuercher@co.meeker.mn.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MASTER = Pattern.compile("([^,]*), *([A-Z/ ]+)(?:, *([A-Z]{2})(?: *\\d{5})?)?(?: (.*))? ([-+]?\\d{2}\\.\\d{6}) ([-+]?\\d{2}\\.\\d{6}) (.*?) None (.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active911 Page")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCity = match.group(2).trim();
    data.strState = getOptGroup(match.group(3));
    String place =  getOptGroup(match.group(4));
    data.strPlace = stripFieldEnd(place, " None");
    setGPSLoc(match.group(5)+','+match.group(6), data);
    data.strCall = match.group(7).trim();

    data.strSupp = INFO_BRK_PTN.matcher(match.group(8).trim()).replaceAll("\n").trim();

    // If address contained a state delimiter, we are good to go.
    // If not, some extra work may be required to split out the city and place fields
    if (data.strState.isEmpty()) {
      String city = append(data.strCity, " ", data.strPlace);
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
      data.strPlace = getLeft();
    }
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CEDAR MILLS",
      "COSMOS",
      "DARWIN",
      "DASSEL",
      "EDEN VALLEY",
      "GROVE CITY",
      "KINGSTON",
      "LITCHFIELD",
      "WATKINS",

      // Unincorporated communities
      "ACTON",
      "BECKVILLE",
      "CORVUSO",
      "CROW RIVER",
      "FOREST CITY",
      "GREENLEAF",
      "JENNIE",
      "LAMSON",
      "MANANNAH",
      "ROSENDALE",
      "STROUT",

      // Townships
      "ACTON TWP",
      "CEDAR MILLS TWP",
      "COLLINWOOD TWP",
      "COSMOS TWP",
      "DANIELSON TWP",
      "DARWIN TWP",
      "DASSEL TWP",
      "ELLSWORTH TWP",
      "FOREST CITY TWP",
      "FOREST PRAIRIE TWP",
      "GREENLEAF TWP",
      "HARVEY TWP",
      "KINGSTON TWP",
      "LITCHFIELD TWP",
      "MANANNAH TWP",
      "SWEDE GROVE TWP",
      "UNION GROVE TWP"
  };

}
