package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MIOttawaCountyParser extends DispatchH05Parser {

  public MIOttawaCountyParser() {
    this("OTTAWA COUNTY", "MI");
  }

  public MIOttawaCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "ID? Type:CALL_PRI! Call_Address:ADDRCITY! Cross_Streets:X! Common_Name:PLACE! GPS? ( Addtl_Location_Info:PLACE! | Additional_Location_Info:PLACE! ) ( Nature:CALL! https:GPS2! Narrative:EMPTY! | https:SKIP! Nature:CALL! Additional_Narrative:EMPTY! Units_Assigned:SKIP! ) INFO_BLK+? Alerts:ALERT? Unit_Times:EMPTY? TIMES+");
    setAccumulateUnits(true);
  }

  @Override
  public String getAliasCode() {
    return "MIOttawaCounty";
  }

  @Override
  public String getFilter() {
    return "@occda.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_PRI")) return new PriorityField(".*[, ]+Pri(?:ority)?: +(\\d+),?|.*()", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("GPS2")) return new MyGPS2Field();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      String city = data.strCity;
      data.strCity = "";
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
      if (data.strCity.isEmpty()) {
        data.strCity = city;
      } else {
        data.strApt = append(data.strApt, "-", stripFieldStart(getLeft(), "#"));
        data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strApt);
      }
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("Lat. Long. *(.*?)(-.*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+','+match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyGPS2Field extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strGPSLoc.isEmpty()) return;
      int pt = field.indexOf("query=");
      if (pt < 0) return;
      super.parse(field.substring(pt+6).trim(), data);
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "COOPERSVILLE CITY",
      "FERRYSBURG",
      "GRAND HAVEN",
      "HOLLAND",
      "HOLLAND CITY",
      "HUDSONVILLE",
      "ZEELAND",
      "VILLAGE",
      "SPRING LAKE",

      // Census-designated places
      "ALLENDALE",
      "BEECHWOOD",
      "JENISON",

      // Other unincorporated communities
      "AGNEW",
      "BAUER",
      "BEAVERDAM",
      "BIG SPRING",
      "BLENDON",
      "BORCULO",
      "CONGER",
      "CONKLIN",
      "CRISP",
      "CROCKERY LAKE",
      "DENNISON",
      "DRENTHE",
      "EASTMANVILLE",
      "FINNASEY",
      "FOREST GROVE",
      "GOODING",
      "GRAND VALLEY",
      "GVSU",
      "HARLEM STATION",
      "HARRISBURG",
      "HERRINGTON",
      "JAMESTOWN",
      "LAMONT",
      "LISBON",
      "MACATAWA",
      "MARNE",
      "NORTH BLENDON",
      "NUNICA",
      "OTTAWA CENTER",
      "PEARLINE",
      "PORT SHELDON",
      "RENO",
      "ROBINSON",
      "SPOONVILLE",
      "TALLMADGE",
      "VRIESLAND",
      "WEST OLIVE",
      "WRIGHT",

      // Townships
      "ALLENDALE TWP",
      "BLENDON TWP",
      "CHESTER TWP",
      "CROCKERY TWP",
      "GEORGETOWN TWP",
      "GRAND HAVEN TWP",
      "HOLLAND TWP",
      "JAMESTOWN TWP",
      "OLIVE TWP",
      "PARK TWP",
      "POLKTON TWP",
      "PORT SHELDON TWP",
      "ROBINSON TWP",
      "SPRING LAKE TWP",
      "TALLMADGE TWP",
      "WRIGHT TWP",
      "ZEELAND TWP",

      // Kent County
      "KENT COUNTY",

      // Cities
      "CEDAR SPRINGS",
      "EAST GRAND RAPIDS",
      "GRAND RAPIDS",
      "GRANDVILLE",
      "KENTWOOD",
      "LOWELL",
      "ROCKFORD",
      "WALKER",
      "WYOMING",

      // Villages
      "CALEDONIA",
      "CASNOVIA",
      "KENT CITY",
      "SAND LAKE",
      "SPARTA",

      // Charter townships
      "CALEDONIA CHARTER TWP",
      "CASCADE CHARTER TWP",
      "GAINES CHARTER TWP",
      "GRAND RAPIDS CHARTER TWP",
      "LOWELL CHARTER TWP",
      "PLAINFIELD CHARTER TWP",

      // Civil TWPs
      "ADA TWP",
      "ALGOMA TWP",
      "ALPINE TWP",
      "BOWNE TWP",
      "BYRON TWP",
      "CANNON TWP",
      "COURTLAND TWP",
      "GRATTAN TWP",
      "NELSON TWP",
      "OAKFIELD TWP",
      "SOLON TWP",
      "SPARTA TWP",
      "SPENCER TWP",
      "TYRONE TWP",
      "VERGENNES TWP",

      // Census-designated places
      "BYRON CENTER",
      "CANNONSBURG",
      "COMSTOCK PARK",
      "CUTLERVILLE",
      "FOREST HILLS",
      "NORTHVIEW",

      // Other unincorporated communities
      "ADA",
      "ALASKA",
      "ALTO",
      "BELMONT",
      "CASCADE",
      "CHAUNCEY",
      "DUTTON",
      "ENGLISHVILLE",
      "PARNELL",

      "MUSKEGON COUNTY"
  };
}
