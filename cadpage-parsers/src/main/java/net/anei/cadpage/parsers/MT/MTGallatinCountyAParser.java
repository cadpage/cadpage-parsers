package net.anei.cadpage.parsers.MT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MTGallatinCountyAParser extends SmartAddressParser {
  
  public MTGallatinCountyAParser() {
    super(CITY_CODES, "GALLATIN COUNTY", "MT");
    setFieldList("SRC CALL PRI ADDR APT CITY PLACE GPS MAP UNIT INFO");
  }

  @Override
  public String getFilter() { return "@bozeman.net,@gallatin.mt.gov"; }
  
  private static final Pattern PRIORITY_PATTERN
    = Pattern.compile("(.*)\\b(\\dA),(.*)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.toUpperCase().contains("HIPLINK")) return false;

    Matcher m = PRIORITY_PATTERN.matcher(body);
    if (!m.matches()) return false;

    if (!parseCall(m.group(1).trim(), data)) return false;
    data.strPriority = m.group(2);
    parseLocation(m.group(3).trim(), data);
    return true;
  }

  private static final Pattern CALL_PATTERN
    = Pattern.compile("(.*?)(?:, *)?ra:(.*?),(.*?),?");
  private boolean parseCall(String field, Data data) {
    Matcher m = CALL_PATTERN.matcher(field);
    if (m.matches()) {
      data.strSupp = m.group(1).trim();
      data.strSource = m.group(2).trim();
      data.strCall = m.group(3).trim();
      return true;
    }
    return false;
  }
  
  private void parseLocation(String field, Data data) {
    // Split on "," except where GPS delimiter
    String[] subfield = field.split("(?<!\\.\\d{4}),");
    parseAddressAndPlace(subfield[0].trim(), data);
    for (int i=1; i<subfield.length; i++)
      parseLocationSubfield(i, subfield[i].trim(), data);
  }

  private void parseAddressAndPlace(String field, Data data) {
    int cPtr = field.indexOf(':');
    if (cPtr > -1) {
      int pPtr = field.indexOf('(');
      pPtr = field.indexOf(')', pPtr+1);
      if (pPtr > -1) {
        cPtr = field.indexOf(':', pPtr+1);
      }
    }
    if (cPtr > -1) {
      parsePlace(field.substring(cPtr+2), data);
      field = field.substring(0, cPtr);
    }
    parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK | FLAG_ANCHOR_END, field, data);
  }
  
  void parsePlace(String field, Data data) {
    if (field.equals("EST")) {
      data.strSupp = append(data.strSupp, "/", "EST");
      return;
    }
    field = stripFieldStart(field, "@");
    field = field.replace(": @", " - ");
    data.strPlace = append(data.strPlace, " - ", field);
  }
  
  private static final Pattern APT_PLACE_PATTERN
    = Pattern.compile("(.*?):(.*)");
  private static final Pattern MAP_PATTERN
    = Pattern.compile("m:(.*)");
  private static final Pattern UNIT_PATTERN
    = Pattern.compile("GTAC.*");
  private void parseLocationSubfield(int ndx, String field, Data data) {
    Matcher m = MAP_PATTERN.matcher(field);
    if (m.matches()) {
      data.strMap = m.group(1).trim();
      return;
    }
    m = UNIT_PATTERN.matcher(field);
    if (m.matches()) {
      data.strUnit = append(data.strUnit, " ", field);
      return;
    }
    if (ndx == 1) {
      if (field.length() < 5) {
        data.strApt = field;
        return;
      }
      m = APT_PLACE_PATTERN.matcher(field);
      if (m.matches()) {
        data.strApt = m.group(1);
        parsePlace(m.group(2).trim(), data);
        return;
      }
      if (data.strPlace.equals("")) {
        parsePlace(field, data);
        return;
      }
    }
    data.strSupp = append(data.strSupp, "/", field);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[] {
    "GALL", "GALLATIN COUNTY",
      
// Adjacent counties
    "MADI", "MADISON COUNTY",
    "JEFF", "JEFFERSON COUNTY",
    "BROA", "BROADWATER COUNTY",
//    MEAGHER COUNTY
//    PARK COUNTY
//    PARK COUNTY, WY
//    TETON COUNTY, WY
//    FREMONT COUNTY, ID

//    Cities
    "BELG", "BELGRADE",
    "BOZE", "BOZEMAN",
    "THRE", "THREE FORKS",

// Towns
    "MANH", "MANHATTAN",
//    WEST YELLOWSTONE

// Census-designated places
//    AMSTERDAM
//    CHURCHILL
    "BSKY", "BIG SKY",
//    FOUR CORNERS
//    GALLATIN RIVER RANCH,
//    HEBGEN LAKE ESTATES
//    KING ARTHUR PARK
//    PONDEROSA PINES
//    SEDAN
//    SPRINGHILL
//    WILLOW CREEK

// Unincorporated communities
    "LOGN", "LOGAN",
//    MAUDLOW
  });
}
