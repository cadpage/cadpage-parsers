package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAPlacerCountyCParser extends FieldProgramParser {

  public CAPlacerCountyCParser() {
    super("PLACER COUNTY", "CA",
          "Call:CALL1! Place:PLACE! ADDR:ADDRCITY! City:CITY! District:MAP! Map:MAP/S! Units:UNIT! Narrative:INFO! INFO/N+ Incidents:ID! CFS:SKIP! Primary:SKIP!");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    return super.getField(name);
  }

  private class MyCall1Field extends CallField {

    public MyCall1Field() {
      super("[A-Z]+", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "AUTOAID","Automatic Aid Request",
      "BOMB",   "Bomb Threat",
      "BOXCAR", "Boxcar Fire",
      "CALARM", "Commercial Fire Alarm",
      "CGAS",   "Commercial Gas Leak",
      "CO",     "Carbon Monoxide Alarm",
      "CSTRUH", "Commercial Structure (High)",
      "CSTRUL", "Commercial Structure (Low)",
      "ELEV",   "Elevator Rescue",
      "FINV",   "Fire Investigation",
      "FLOOD",  "Flooding",
      "FTRASH", "Trash Fire",
      "FVEH",   "Vehicle Fire",
      "HWIRE",  "Hazardous Wires",
      "HYD",    "Broken Hydrant",
      "HZH",    "Hazmat(High)",
      "HZL",    "Hazmat(Low)",
      "LAND",   "Helicopter Landing Zone",
      "LOCKH",  "Lock In (High)",
      "LOCKL",  "Lock In (Low)",
      "MAID",   "Medical Aid",
      "MUTAID", "Mutual Aid",
      "PAST",   "Public Assistance",
      "PLANE",  "Plane Crash",
      "POLICE", "Police Assist",
      "RALARM", "Residential Alarm",
      "RESCUE", "Rescue",
      "RGAS",   "Residential Gas Leak",
      "RSTRUH", "Residential Structure (High)",
      "RSTRUL", "Residential Structure (Low)",
      "STRIKE", "Strike Team Request",
      "TRAIN",  "Train Wreck / Derailment",
      "VAF",    "Vehicle Accident with Fire",
      "VAH",    "Vehicle Accident (High)",
      "VAL",    "Vehicle Accident (Low)",
      "VEGH",   "Vegetation Fire (High)",
      "VEGL",   "Vegetation Fire (Low)",
      "WFLOW",  "Water Flow"

  });
}
