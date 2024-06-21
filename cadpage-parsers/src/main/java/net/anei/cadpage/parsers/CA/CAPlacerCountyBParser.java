package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
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

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("C")) {
      data.strState = "CA";
      data.strCity = "";
      int pt = data.strAddress.indexOf(',');
      if (pt >= 0) {
        data.strCity = data.strAddress.substring(pt+1).trim();
        data.strAddress = data.strAddress.substring(0,pt).trim();
      }
    }
    return true;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "187",      "ATTEMPTED MURDER",
      "207",      "KIDNAPPING",
      "211",      "ROBBERY IN PROGRESS",
      "215",      "CARJACKING",
      "245",      "ASSAULT W DEADLY WEAPON",
      "246",      "SHOOT AT DWELLING",
      "261",      "RAPE IN PROGRESS",
      "451",      "ARSON",
      "459",      "BURGLARY",
      "487",      "GRAND THEFT",
      "664",      "ATTEMPTED MURDER",
      "1180",     "MAJOR INJURY ACCIDENT",
      "1181",     "MINOR INJURY ACCIDENT",
      "1182",     "NON INJURY ACCIDENT",
      "1183",     "VEH ACCIDENT UNKNOWN",
      "2800",     "PURSUIT",
      "BOMB",     "BOMB THREAT",
      "CALARM",   "COMMERCIAL FIRE ALARM",
      "CGAS",     "COMMERCIAL GAS LEAK",
      "CITASST",  "CIT ASSIST",
      "CO",       "CARBON MONIXIDE ALARM",
      "CODE 10",  "BOMB THREAT",
      "CODE 8",   "CODE 8",
      "CODE 9",   "ROBBER ALARM",
      "CPR",      "CARDIAC ARREST",
      "CSTRUC",   "COMMERCIAL STRUCTURE FIRE",
      "CSTRUH",   "COMMERCIAL STRUCTURE FIRE HIGH",
      "CSTRUL",   "COMMERCIAL STRUCTURE FIRE LOW",
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
      "MAID2",    "CODE 2 MEDICAL AID",
      "MDOWN",    "MAN DOWN",
      "MUTAID",   "MUTUAL AID REQUEST",
      "PAST",     "PUBLIC ASSIST",
      "PEDCK",    "PEDESTRIAN CHECK",
      "PLANE",    "PLANE DOWN",
      "POLICE",   "POLICE ASSIST",
      "RALARM",   "RESIDENTIAL FIRE ALARM",
      "RESCUE",   "TECHNICIAL RESCUE",
      "RGAS",     "RESIDENTIAL GAS LEAK",
      "RSTRUC",   "RESIDENTIAL STRUCTURE FIRE",
      "RSTRUH",   "RESIDENTIAL STRUCTURE FIRE - HIGH",
      "RSTRUL",   "RESIDENTIAL STRUCTURE FIRE - LOW",
      "RWATER",   "WATER RESCUE",
      "SIG2000",  "BAIT CAR",
      "SIG3000",  "BANK GPS ALARM",
      "STRIKE",   "STRIKE TEAM REQUEST",
      "TEST",     "TEST",
      "TRAIN",    "TRAIN ACCIDENT",
      "TSTOP",    "TRAFFIC STOP",
      "VAF",      "VEHICLE ACCIDENT - FIRE",
      "VAH",      "VEHICLE ACCIDENT - HIGH",
      "VAL",      "VEHICLE ACCIDENT - LOW",
      "VEGHI",    "VEGETATION FIRE - HIGH"

  });
}
