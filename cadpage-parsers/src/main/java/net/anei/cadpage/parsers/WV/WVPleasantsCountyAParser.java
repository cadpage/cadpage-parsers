package net.anei.cadpage.parsers.WV;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class WVPleasantsCountyAParser extends DispatchEmergitechParser {
  
  public WVPleasantsCountyAParser() {
    super(CITY_LIST, "PLEASANTS COUNTY", "WV");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (OH_CITIES.contains(data.strCity)) data.strState = "OH";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "ST MARYS",
    "SAINT MARYS",
    
    // Towns
    "BELMONT",
    
    // Magisterial districts
    "GRANT",
    "JEFFERSON",
    "LAFAYETTE",
    "MCKIM",
    "UNION",
    "WASHINGTON",
    
    // Unincorporated communities
    "ARVILLA",
    "CALCUTTA",
    "HEBRON",
    "PINE GROVE",
    "VAUCLUSE",
    
    // Tyler County
    "FRIENDLY",
    
    // Wood County
    "NEWPORT",
    "WAVERLY",
    
    // Washington County, OH
    "MATAMORAS",
    "NEW MATAMORAS"
  };
  
  private static final Set<String> OH_CITIES = new HashSet<>(Arrays.asList("MATAMORAS", "NEW MATAMORAS"));

}
