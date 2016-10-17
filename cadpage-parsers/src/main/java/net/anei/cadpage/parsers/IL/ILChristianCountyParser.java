package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser.Result;
import net.anei.cadpage.parsers.SmartAddressParser.StartType;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Christian County, IL
 */
public class ILChristianCountyParser extends DispatchEmergitechParser {
  
  public ILChristianCountyParser() {
    super("ChristianCounty911:", CITY_LIST, "CHRISTIAN COUNTY", "IL", TrailAddrType.PLACE);
  }
  
  public String getFilter() {
    return "panafiredepartment@gmail.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR2")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@',  '/');
      super.parse(field, data);
    }
  }

  @Override
  protected Result parseAddress(StartType sType, int flags, String address) {
    if (sType == StartType.START_PLACE) sType = StartType.START_ADDR;
    return super.parseAddress(sType, flags, address);
  }

  private static final String[] CITY_LIST = new String[]{
      
      //Cities
      
      "ASSUMPTION",
      "PANA",
      "TAYLORVILLE",

      //Villages

      "BULPITT",
      "EDINBURG",
      "HARVEL",
      "JEISYVILLE",
      "KINCAID",
      "MORRISONVILLE",
      "MOUNT AUBURN",
      "MOWEAQUA",
      "OWANECO",
      "PALMER",
      "STONINGTON",
      "TOVEY",

      //Unincorporated
      "CLARKSDALE",
      "DUNKEL",
      "HEWITTSVILLE",
      "LANGLEYVILLE",
      "MILLERSVILLE",
      "OLD STONINGTON",
      "RADFORD",
      "ROSAMOND",
      "SHARPSBURG",
      "WILLEY STATION",
      
      //Townships
      "ASSUMPTION",
      "BEAR CREEK",
      "BUCKHART",
      "GREENWOOD",
      "JOHNSON",
      "KING",
      "LOCUST",
      "MAY",
      "MOSQUITO",
      "MOUNT AUBURN",
      "PANA TOWNSHIP",
      "PRAIRIETON",
      "RICKS",
      "ROSAMOND",
      "SOUTH FORK",
      "STONINGTON",
      "TAYLORVILLE",
      
      // Montgomery County
      "NOKOMIS"
  };
}
