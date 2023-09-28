package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class CTWindhamCountyCParser extends FieldProgramParser {

  public CTWindhamCountyCParser() {
    super(CITY_LIST, "WINDHAM COUNTY", "CT",
          "ADDRCITY APT PLACE PRI CALL INFO UNIT X DATETIME ID! END");
  }

  private static final Pattern MASTER = Pattern.compile("([^,]+), *(.*?) Cross Street (.*?) ?(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) (\\d{4}-\\d{8}) \\(\\S+\\)");
  private static final Pattern CHANNEL_PTN = Pattern.compile("\\d\\d\\.\\d\\d");
  private static final Pattern LEAD_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM) *(\\S+) +(.*?)");
  private static final Pattern CALL_PTN = Pattern.compile("(.*?)\\b((?:[AB]LS|Vehicle Accident)\\b.*)");

  @Override
  public boolean parseMsg(String body, Data data) {

    String[] flds = body.split(";");
    if (flds.length >= 10) {
      return parseFields(flds, data);
    }

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    setFieldList("ADDR CITY APT PLACE CALL UNIT CH X DATE TIME ID");
    parseAddress(match.group(1).trim().replace('@', '&'), data);
    body = match.group(2).trim();
    data.strCross = match.group(3).trim();
    data.strDate =  match.group(4);
    data.strTime =  match.group(5);
    data.strCallId =  match.group(6);

    Parser p = new Parser(body);
    String unit = p.getLast(' ');
    if (CHANNEL_PTN.matcher(unit).matches()) {
      data.strChannel = unit;
      unit = p.getLast(' ');
    }
    data.strUnit = unit;
    body = p.get();

    parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body, data);
    if (data.strCity.isEmpty()) return false;
    body = getLeft();

    match = LEAD_APT_PTN.matcher(body);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      body = match.group(2);
    }

    match = CALL_PTN.matcher(body);
    if (match.matches()) {
      data.strPlace = match.group(1).trim();
      data.strCall = match.group(2);
    } else {
      data.strCall = body;
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("PRI")) return new PriorityField("[1-5]", true);
    if (name.equals("X")) return new CrossField("Cross Street *(.*)", true);
    if (name.equals("DATETIME")) return new TimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID"))  return new IdField("(\\S+) *.*", false);
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|LOT|ROOM) +(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      field = field.toUpperCase();
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    String mapCity = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (mapCity != null) city = mapCity;
    return city;
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[] {
      "UCONN",            "STORRS",
      "WINDHAM CENTER",   "WINDHAM"
  });

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "ANDOVER",
      "BOLTON",
      "COLUMBIA",
      "COVENTRY",
      "ELLINGTON",
      "HEBRON",
      "MANSFIELD",
      "SOMERS",
      "STAFFORD",
      "TOLLAND",
      "UNION",
      "VERNON",
      "WILLINGTON",

      // Other communities
      "AMSTON",
      "COVENTRY LAKE",
      "CRYSTAL LAKE",
      "GILEAD",
      "HEBRON CENTER",
      "HYDEVILLE",
      "MASHAPAUG",
      "MANSFIELD CENTER",
      "ROCKVILLE",
      "SOMERS CENTER",
      "SOUTH COVENTRY",
      "STAFFORD HOLLOW",
      "STAFFORD SPRINGS",
      "STORRS",
      "UCONN",

      // Windham County
      "ASHFORD",
      "BROOKLYN",
      "BROOKLYN",
      "CANTERBURY",
      "CENTRAL VILLAGE",
      "CHAPLIN",
      "DANIELSON",
      "DAYVILLE",
      "EAST BROOKLYN",
      "EASTFORD",
      "FABYAN",
      "HAMPTON",
      "KILLINGLY",
      "LAKE BUNGEE",
      "LAKE CHAFFEE",
      "MECHANICSVILLE",
      "MOOSUP",
      "NORTH GROSVENORDALE",
      "NORTH WINDHAM",
      "ONECO",
      "PLAINFIELD",
      "PLAINFIELD VILLAGE",
      "POMFRET",
      "PUTNAM",
      "PUTNAM DISTRICT",
      "QUASSET LAKE",
      "QUINEBAUG",
      "SCOTLAND",
      "SOUTH WINDHAM",
      "SOUTH WOODSTOCK",
      "STERLING",
      "THOMPSON",
      "WAUREGAN",
      "WILLIMANTIC",
      "WINDHAM",
      "WINDHAM CENTER",
      "WITCHES WOODS",
      "WOODSTOCK",

      // New London County
      "LEBANON",

      // Tolland County
      "MANSFIELD"

  };
}
