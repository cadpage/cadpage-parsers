package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Placer County, CA (B)
 */
public class CAPlacerCountyBParser extends DispatchA20Parser {

  public CAPlacerCountyBParser() {
    super(CALL_CODES, "PLACER COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "@rocklin.ca.us,@ci.lincoln.ca.us,@lincolnca.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "BOMB",     "BOMB THREAT",
      "CALARM",   "COMMERCIAL FIRE ALARM",
      "CGAS",     "COMMERCIAL GAS LEAK",
      "CO",       "CARBON MONIXIDE ALARM",
      "CPR",      "CARDIAC ARREST",
      "CSTRUC",   "COMMERCIAL STRUCTURE FIRE",
      "CSTRUH",   "COMMERCIAL STRUCTURE FIRE - HIGH",
      "CSTRUL",   "COMMERCIAL STRUCTURE FIRE - LOW",
      "FINV",     "FIRE INVESTIGATION",
      "FIREAID",  "FIREAID",
      "FLOOD",    "FLOODING INCIDENT",
      "FRAUD",    "FRAUD",
      "FTEST",    "FIRE TEST INCIDENT",
      "FTRASH",   "TRASH FIRE",
      "FVEG",     "VEGETATION FIRE",
      "FVEH",     "VEHICLE FIRE",
      "FVSUMM",   "VEGETATION FIRE SUMMER",
      "FVWIN",    "VEGETATION FIRE WINTER",
      "HWIRE",    "HAZARDOUS WIRES",
      "HZH",      "HAZMAT - HIGH",
      "HZL",      "HAZMAT - LOW",
      "LAND",     "LANDING ZONE",
      "MAID",     "MEDICAL AID",
      "MAID2",    "MEDICAL AID",
      "MDOWN",    "MAN DOWN",
      "MUTAID",   "MUTUAL AID REQUEST",
      "PAST",     "PUBLIC ASSIST",
      "PLANE",    "PLANE CRASH",
      "POLICE",   "POLICE ASSIST",
      "RALARM",   "RESIDENTIAL FIRE ALARM",
      "RESCUE",   "TECHNICIAL RESCUE",
      "RGAS",     "RESIDENTIAL GAS LEAK",
      "RSTRUC",   "RESIDENTIAL STRUCTURE FIRE",
      "RSTRUH",   "RESIDENTIAL STRUCTURE FIRE - HIGH",
      "RSTRUL",   "RESIDENTIAL STRUCTURE FIRE - LOW",
      "RWATER",   "WATER RESCUE",
      "STRIKE",   "STRIKE TEAM REQUEST",
      "TEST",     "TEST",
      "TRAIN",    "TRAIN ACCIDENT",
      "VAF",      "VEHICLE ACCIDENT - FIRE",
      "VAH",      "VEHICLE ACCIDENT - HIGH",
      "VAL",      "VEHICLE ACCIDENT - LOW",
      "VEGHI",    "VEGETATION FIRE - HIGH"

  });
}
