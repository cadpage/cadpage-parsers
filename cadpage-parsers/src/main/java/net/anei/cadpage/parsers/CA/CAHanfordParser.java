package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Hanford, CA
 */
public class CAHanfordParser extends DispatchA20Parser {
  
  public CAHanfordParser() {
    super(CALL_CODES, "HANFORD", "CA");
  }
  
  @Override
  public String getFilter() {
    return "@cityofhanfordca.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "ZAMB",            "Medical Aid",
      "ZFALARM",         "Fire Alarm",
      "ZFALARMWF",       "Water Flow Fire Alarm",
      "ZFIREAPT",        "Apartment Fire",
      "ZFIRECOM",        "Commercial Fire",
      "ZFIREO",          "Fire Other-Dumpster",
      "ZFIRES",          "Structure Fire/Fire Threatening Structure",
      "ZFIREV",          "Vehicle Fire",
      "ZFIREVEG",        "Grass Fire/Tree/Any Vegetation",
      "ZGASLEKI",        "Gas Leak in Structure",
      "ZGASLEKO",        "Gas Leak Outdoors",
      "ZGASSMI",         "Smell of Gas Indoors",
      "ZGASSMO",         "Smell of Gas Outdoors",
      "ZGENERAL",        "General Alarm",
      "ZHAZCON",         "Hazardous Condition/Wires Down/Spill",
      "ZHAZMAT",         "Hazardous Materials",
      "ZILLBURN",        "Illegal Burn",
      "ZMANDWN",         "Man Down",
      "ZMUT.AMB",        "Mutual Aid/All Ambulance",
      "ZMUTUAL",         "Mutual Aid",
      "ZOESRUN OES",     "Strike Team",
      "ZPA",             "Public Assist",
      "ZSMOKEI",         "Smell of Smoke Inside of Structure",
      "ZSMOKEO",         "Smell of Smoke Outdoors",
      "ZTAFWY",          "Traffic Accident on Freeway",
      "ZTAINJ",          "Traffic Accident with Injury",
      "ZTAPIN",          "Traffic Accident/Pin-in/Rollover/Into Building",
      "ZTRAP",           "Person Trapped/Possible Rescue"


  });
}
