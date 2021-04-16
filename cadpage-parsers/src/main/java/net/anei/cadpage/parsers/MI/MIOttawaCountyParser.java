package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MIOttawaCountyParser extends DispatchH05Parser {

  public MIOttawaCountyParser() {
    super(CITY_LIST, "OTTAWA COUNTY", "MI",
          "ID? Type:CALL_PRI! Call_Address:ADDRCITY! Cross_Streets:X! Common_Name:PLACE! GPS? ( Addtl_Location_Info:PLACE! | Additional_Location_Info:PLACE! ) ( Nature:CALL! https:SKIP! Narrative:EMPTY! | https:SKIP! Nature:CALL! Additional_Narrative:EMPTY! Units_Assigned:UNIT! ) INFO_BLK+? Alerts:ALERT? Unit_Times:EMPTY? TIMES+");
  }

  @Override
  public String getFilter() {
    return "@occda.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_PRI")) return new PriorityField(".*[, ]+Pri(?:ority)?: +(\\d+),?|.*()", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
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
      "KENT COUNTY"
  };
}
