package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class SCBerkeleyCountyParser extends DispatchB2Parser {

  public SCBerkeleyCountyParser() {
    super(CITY_LIST, "BERKELEY COUNTY", "SC");
    setupCallList((CodeSet)null);
   }
  
  @Override
  public String adjustMapCity(String city) {
    String tmp = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) city = tmp;
    return city;
  }

  @Override
  protected Result parseAddress(StartType sType, int flags, String address) {
    address = address.replace('@', '&');
    return super.parseAddress(sType, flags, address);
  }

  private static final String[] CITY_LIST = new String[]{

      //Cities
      "CHARLESTON",
      "GOOSE CREEK",
      "HANAHAN",
      "NORTH CHARLESTON",

      //Towns
      "BONNEAU",
      "JAMESTOWN",
      "MONCKS CORNER",
      "ST STEPHEN",
      "SUMMERVILLE",

      //Townships
      "CROSS",
      "GUMVILLE",
      "LADSON",
      "PINOPOLIS",
      
      // Unincorporated communities
      "CORDESVILLE",
      "HUGER",
      "PINEVILLE",
      "SANGAREE",
      "SANTEE CIRCLE",
      
      // Other neighborhoods
      "LAKE MOULTRIE",
      "LEBANON",
      "MACEDONIA",
      "SPRING LAKE VILLAGE",
      "VILLAGE GREEN",
      "WOODSIDE",
      
      // Charleston County
      "CAINHOY",
      "WANDO",
      
      // Dorchester County
      "RIDGEVILLE",
      
      // Orangeburg County
      "HOLLY HILL"
  };
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "CAINHOY",                 "CHARLESTON",
      "LAKE MOULTRIE",           "BONNEAU",
      "MACEDONIA",               "BONNEAU",
      "SANTEE CIRCLE",           "BONNEAU",
      "SPRING LAKE VILLAGE",     "SUMMERVILLE",
      "VILLAGE GREEN",           "SUMMERVILLE",
      "WOODSIDE",                "LADSON"
  });
}
