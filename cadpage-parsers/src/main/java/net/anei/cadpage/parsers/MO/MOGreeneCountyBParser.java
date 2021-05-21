package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class MOGreeneCountyBParser extends DispatchA52Parser {

  public MOGreeneCountyBParser() {
    super(CALL_CODES, "GREENE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "GRN_Alert@springfieldmo.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Reject MOChristiainCounty alerts
    if (subject.equals("INC:")) return false;

    body = stripFieldStart(body, "GRN_Alert@springfieldmo.gov:");
    body = stripFieldStart(body, "GRN_Alert:");
    if (body.startsWith(":")) body = "LOC" + body;
    else if (!body.startsWith("LOC:")) body = "LOC:"+ body;
    if (!parseMsg(body, data)) return false;
    return data.strAddress.length() > 0;
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_KEEP_STATE_HIGHWAY;
  }

  private static final Pattern HOUSE_NUM_PTN = Pattern.compile("(?:1|\\d{3,5}) +");
  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    sAddress = FARM_ROAD.matcher(sAddress).replaceAll("$1 FARM ROAD $2");
    sAddress = STATE_HWY.matcher(sAddress).replaceAll("$1 STATE HIGHWAY $2");
    sAddress = STATE_HWY_HWY.matcher(sAddress).replaceAll("$1");

    // Cross streets include a house number that is not useful for this purpose
    if (cross) {
      sAddress = HOUSE_NUM_PTN.matcher(sAddress).replaceFirst("");
    }
    return sAddress;
  }

  private static Pattern FARM_ROAD = Pattern.compile("\\b([NESW]) FR(\\d+)\\b");
  private static Pattern STATE_HWY = Pattern.compile("\\b([NESW]) SH(([A-Z])(?:\\3)?)\\b");
  private static Pattern STATE_HWY_HWY = Pattern.compile("\\b([NESW] STATE HIGHWAY [^ ]+) HWY\\b");

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "FALARM",   "FIRE ALARM",
      "FAMBA",    "AMBULANCE ASSIST",
      "FAUTO",    "AUTOMOBILE  FIRE",
      "FBLDG",    "BUILDING FIRE",
      "FBOMB",    "EXPLOSIVE DEVICE",
      "FBOMBT",   "BOMB THREAT",
      "FELEV",    "ELEVATOR EMERGENCY",
      "FEMS",     "MEDICAL EMERGENCY",
      "FEMS2",    "MEDICAL EMERGENCY",
      "FGBLDG",   "GAS ODOR IN A STRUCTURE",
      "FGOTS",    "GAS ODOR OUTSIDE",
      "FHOUSE",   "HOUSE FIRE",
      "FHZMAT",   "HAZARDOUS MATERIALS",
      "FMCI",     "MASS CASUALTY INCIDENT",
      "FMVA",     "MOTOR VEHICLE ACCIDENT",
      "FODOR",    "ODOR INVESTIGATION",
      "FOTS",     "FIRE OUTSIDE",
      "FOUT",     "FIRE EXTINGUISHED",
      "FPLANE",   "AIRCRAFT EMERGENCY",
      "FPTRAP",   "PERSONS TRAPPED",
      "FSMOKA",   "SMOKE INVESTIGATION",
      "FSPILL",   "FUEL SPILL",
      "FSVC",     "SERVICE CALL",
      "FSVCSE",   "A FIRE SERVICE CALL",
      "FTRAIN",   "TRAIN FIRE",
      "FTRUCK",   "TRUCK FIRE",
      "FWATER",   "WATER RESCUE",
      "FWIRE",    "WIRE DOWN"
  });

}
