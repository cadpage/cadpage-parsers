package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILLakeCountyCParser extends FieldProgramParser {

  public ILLakeCountyCParser() {
    super(CITY_LIST, "LAKE COUNTY", "IL",
          "DATETIMECALL ADDRCITYPLACE X UNIT MAP! ID? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "nw_dispatch_noreply@glenview.il.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISPATCH")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIMECALL")) return new MyDateTimeCallField();
    if (name.equals("ADDRCITYPLACE")) return new MyAddressCityPlaceField();
    if (name.equals("MAP"))  return new MyMapField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d+\\b.*", true);
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_CALL_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)[: ](\\S.*)");
  private class MyDateTimeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCall = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL";
    }
  }

  private class MyAddressCityPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field.substring(0,pt).trim(), data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field.substring(pt+1).trim(), data);
        if (data.strCity.isEmpty()) {
          data.strCity = getLeft();
        } else {
          data.strPlace = getLeft();
        }
      }
      else {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT, field, data);
        data.strPlace = getLeft();
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }

  private static final Pattern MAP_PTN = Pattern.compile("Grid:(.*)Beat:(.*)");
  private class MyMapField extends MapField {
    public void parse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strMap = append(match.group(1).trim(), "/", match.group(2).trim());
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "HIGHLAND PARK",
      "HIGHWOOD",
      "LAKE FOREST",
      "MCHENRY",
      "NORTH CHICAGO",
      "PARK CITY",
      "WAUKEGAN",
      "ZION",

      // Villages
      "ANTIOCH",
      "BANNOCKBURN",
      "BARRINGTON",
      "BARRINGTON HILLS",
      "BEACH PARK",
      "BUFFALO GROVE",
      "DEERFIELD",
      "DEER PARK",
      "FOX LAKE",
      "FOX RIVER GROVE",
      "GRAYSLAKE",
      "GREEN OAKS",
      "GURNEE",
      "HAINESVILLE",
      "HAWTHORN WOODS",
      "INDIAN CREEK",
      "ISLAND LAKE",
      "KILDEER",
      "LAKE BARRINGTON",
      "LAKE BLUFF",
      "LAKE VILLA",
      "LAKE ZURICH",
      "LAKEMOOR",
      "LIBERTYVILLE",
      "LINCOLNSHIRE",
      "LINDENHURST",
      "LONG GROVE",
      "METTAWA",
      "MUNDELEIN",
      "NORTH BARRINGTON",
      "OLD MILL CREEK",
      "PORT BARRINGTON",
      "RIVERWOODS",
      "ROUND LAKE",
      "ROUND LAKE BEACH",
      "ROUND LAKE HEIGHTS",
      "ROUND LAKE PARK",
      "THIRD LAKE",
      "TOWER LAKES",
      "VERNON HILLS",
      "VOLO",
      "WADSWORTH",
      "WAUCONDA",
      "WHEELING",
      "WINTHROP HARBOR",

      // Census-designated places
      "CHANNEL LAKE",
      "FOREST LAKE",
      "FOX LAKE HILLS",
      "GAGES LAKE",
      "GRANDWOOD PARK",
      "KNOLLWOOD",
      "LAKE CATHERINE",
      "LONG LAKE",
      "VENETIAN VILLAGE",

      // Unincorporated communities
      "APTAKISIC",
      "DIAMOND LAKE",
      "EDDY",
      "FORT SHERIDAN",
      "FREMONT CENTER",
      "GILMER",
      "GRANGE HALL",
      "GRASS LAKE",
      "HALF DAY",
      "INGLESIDE",
      "IVANHOE",
      "KENNEDY",
      "LOON LAKE",
      "MILLBURN",
      "MONAVILLE",
      "PALM BEACH",
      "PRAIRIE VIEW",
      "RONDOUT",
      "ROSECRANS",
      "RUSSELL",
      "SYLVAN LAKE",
      "WEST MILTMORE",
      "WILDWOOD",
      "WILSON",

      // Townships
      "ANTIOCH",
      "AVON",
      "BENTON",
      "CUBA",
      "ELA",
      "FREMONT",
      "GRANT",
      "LAKE VILLA",
      "LIBERTYVILLE",
      "MORAINE",
      "NEWPORT",
      "SHIELDS",
      "VERNON",
      "WARREN",
      "WAUCONDA",
      "WAUKEGAN",
      "WEST DEERFIELD",
      "ZION",

      // Cook County
      "GLENCOE",
      "GLENVIEW"
  };

}
