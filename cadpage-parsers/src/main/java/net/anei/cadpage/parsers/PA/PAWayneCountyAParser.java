package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAWayneCountyAParser extends FieldProgramParser {

  public PAWayneCountyAParser() {
    super(CITY_LIST, "WAYNE COUNTY", "PA",
          "CALL INFO ADDRCITY/S X UNIT GPS DATETIME! END");
  }

  @Override
  public String getFilter() {
    return "Dispatch@wcpa911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      boolean forceCity = false;
      if (field.endsWith(" BOROUGH")) {
        forceCity = true;
        field = field.substring(0,field.length()-8).trim();
      } else if (field.endsWith(" BORO")) {
        forceCity = true;
        field = field.substring(0,field.length()-5).trim();
      } else if (field.endsWith(" TOWNSHIP")) {
        forceCity = true;
        field = field.substring(0, field.length()-9).trim() + " TWP";
      } else if (field.endsWith(" TWP")) {
        forceCity = true;
      }
      super.parse(field, data);
      if (forceCity && data.strCity.isEmpty()) {
        String addr = data.strAddress;
        data.strAddress = "";
        parseAddress(StartType.START_ADDR, addr, data);
        data.strCity = getLeft();
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Boroughs
      "BETHANY",
      "HAWLEY",
      "HONESDALE",
      "PROMPTON",
      "STARRUCCA",
      "WAYMART",

      // Townships
      "BERLIN TWP",
      "BUCKINGHAM TWP",
      "CANAAN TWP",
      "CHERRY RIDGE TWP",
      "CLINTON TWP",
      "DAMASCUS TWP",
      "DREHER TWP",
      "DYBERRY TWP",
      "GREEN TWP",
      "GREENE TWP",
      "LAKE TWP",
      "LEBANON TWP",
      "LEHIGH TWP",
      "MANCHESTER TWP",
      "MOUNT PLEASANT TWP",
      "OREGON TWP",
      "PALMYRA TWP",
      "PAUPACK TWP",
      "PRESTON TWP",
      "SALEM TWP",
      "SCOTT TWP",
      "SOUTH CANAAN TWP",
      "STERLING TWP",
      "TEXAS TWP",

      // Census-designated places
      "BIG BASS LAKE",
      "GOULDSBORO",
      "POCONO SPRINGS",
      "THE HIDEOUT",
      "WALLENPAUPACK LAKE ESTATES",
      "WHITE MILLS",

      // Unincorporated communities
      "DAMASCUS",
      "EQUINUNK",
      "GALILEE",
      "REFLECTION LAKE",
      "HAMLIN",
      "HOLLISTERVILLE",
      "JERICHO",
      "LAKE ARIEL",
      "LAKE COMO",
      "LAKEVILLE",
      "LAKEWOOD",
      "MILANVILLE",
      "NEWFOUNDLAND",
      "ORSON",
      "PLEASANT MOUNT",
      "POYNTELLE",
      "RILEYVILLE",
      "SOUTH STERLING",
      "STARLIGHT",
      "TANNERS FALLS",

      // Other places
      "BREEZEWOOD ACRES",
      "ELK LAKE",
      "HIDDEN LAKE ESTATES",
      "INDIAN COUNTRY",
      "POCONO RANCHETTES",
      "ROCKY ACRES",
      "SIMPSON GENTEX",

      // Lackawanna County
      "CARBONDALE",
      "CITY OF CARBONDALE",
      "CLIFFORD",
      "CLIFFORD TWP",
      "FELL TWP",
      "SIMPSON",
      "THOMPSON TWP",
      "VANDLING",

      // Pike County
      "GREENTOWN",
      "HEMLOCK GROVE",
      "PROMISED LAND",
      "THE ESCAPE",

      // Susquehanna County
      "BROWNDALE",
      "FOREST CITY",
      "HERRICK TWP",
      "UNIONDALE"
  };
}
