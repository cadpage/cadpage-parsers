package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAPlacerCountyCParser extends FieldProgramParser {
  
  public CAPlacerCountyCParser() {
    super(CITY_CODES, "PLACER COUNTY", "CA",
          "( Call:CALL1! Place:PLACE! ADDR:ADDRCITY! City:CITY! District:MAP! Map:MAP/S! Units:UNIT! Narrative:INFO! INFO/N+ Incidents:ID! CFS:SKIP! Primary:SKIP! " +
          "| NOTIFYTYPE:SKIP! CALL:CALL! ADDR:ADDRCITY! CROSSSTREETS:X2! ID:ID! PRI:PRI! DATE:DATETIME2! MAP:SKIP! UNIT:UNIT! INFO:INFO/N+ DISTRICT:MAP! GROUP:MAP/D! AREA:MAP/D! LAT:GPS1! LON:GPS2 END )");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("DATETIME2")) return new MyDateTime2Field();
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
  
  private class MyCross2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("<") && field.endsWith(">")) return;
      field = stripFieldStart(field, "btwn ");
      field = field.replaceAll(" and ", "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATETIME2_PTN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTime2Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATETIME2_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
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
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AP", "APPLEGATE",
      "CO", "COLFAX",
      "FH", "FORESTHILL", 
      "GB", "GRANITE BAY",
      "LO", "LOOMIS",
      "NC", "NEWCASTLE"
  });
}
