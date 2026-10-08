package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchC11Parser;

public class NCStanlyCountyCParser extends DispatchC11Parser {

  public NCStanlyCountyCParser() {
    this("STANLY COUNTY", "NC");
  }

  public NCStanlyCountyCParser(String defCity, String defState) {
    super(defCity, defState, C11_UNIT | C11_INFO);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setCodePattern("\\d{1,2}[_A-Z]+");
    setCodeList(
        "--CHOOSE_SUBTYPE--",
        "ANIMAL_RESCUE",
        "COMMERCIAL",
        "COMMERCIAL_INDUSTRIAL",
        "COMPLAINT",
        "DEFAULT",
        "DETECTOR",
        "ELECTRICAL_ARC",
        "EMERGENCY",
        "EXTINGUISHED",
        "HIGH_LIFE_HAZ",
        "ILLEGAL",
        "LARGE",
        "LARGE_STRUCT",
        "LOCKED_BUILD",
        "LOCKED_VEHICLE",
        "MOBILE_HOME",
        "MOVE-UP",
        "NO_INJURY",
        "NON_DWELLING",
        "NON-EMERGENCY",
        "OTHERMISC",
        "OUTSIDE_COMM",
        "OUTSIDE_ODOR",
        "P129_PER",
        "RESIDENTIAL",
        "RESIDENTIAL_SING",
        "SERVICE_CALL",
        "SERVICE_MED",
        "SMALL",
        "SMALL_NON_DWELL",
        "SMALL_STRUCT",
        "TRANSFORMER",
        "TREE_OBJECT",
        "TREES_OBJECTS_FIRE",
        "UNKNOWN",
        "UNKNOWN_EXPL",
        "VEHICLE_FIRE",
        "VEH_THR_BUILD",
        "WIRES_DOWN",
        "WIRES_DOWN_SMK");
  }

  @Override
  public String getAliasCode() {
    return "NCStanlyCounty";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public int splitBreakLength() { return 500; }
      @Override public int splitBreakPad() { return 6; }
    };
  }

  private static final Pattern GRAND_PT_PTN = Pattern.compile("\\bGRAND +PT\\b");

  @Override
  public String adjustGpsLookupAddress(String addr) {
    addr = addr.toUpperCase();
    addr = GRAND_PT_PTN.matcher(addr).replaceAll("GRAND POINT");
    return addr;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "1620 GRAND POINT LANE",                "+35.553814,-81.490755",
  });
}
