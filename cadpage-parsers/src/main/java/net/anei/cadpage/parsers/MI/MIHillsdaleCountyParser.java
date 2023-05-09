package net.anei.cadpage.parsers.MI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;


/**
 * Hillsdale County, MI
 */
public class MIHillsdaleCountyParser extends FieldProgramParser {

  public MIHillsdaleCountyParser() {
    super("HILLSDALE COUNTY", "MI",
          "CALL ADDR ( X | CITY ST_ZIP? PLACE? X ) DATETIME!");
  }

  @Override
  public String getFilter() {
    return "hccd@co.hillsdale.mi.us";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split(","), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ST_ZIP")) return new StateField("([A-Z]{2})(?: +\\d{5})?", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyCallField extends Field {
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

  private class MyCrossField extends CrossField {

    public MyCrossField() {
      super(".*//.*", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "911-HNGUP",  "911 HANG UP",
      "911-MISRTE", "911 MISROUTE",
      "ACC-CRDR",   "MVA - CAR/DEER",
      "ACC-H&R",    "MVA - HIT & RUN",
      "ACC-OTHER",  "MVA - OTHER",
      "ACC-PDC",    "PROPERTY DAMAGE CRASH",
      "ACC-PI",     "MVA - MOTOR VEHICLE CRASH",
      "ACC-PP",     "MVA - PRIVATE PROPERTY CRASH",
      "ACC-UNK",    "MVA - UNKNOWN INJURIES",
      "AIRCEMRG",   "FIRE - AIRCRAFT EMERGENCY",
      "ALARM-BUS",  "FIRE - COMMERCIAL ALARM",
      "ALARM-CO",   "FIRE - CARBON MONIXIDE ALARM",
      "ALARM-FIR",  "FIRE - FIRE ALARM",
      "ALARM-MED",  "MED - MEDICAL ALARM",
      "ALARM-RES",  "FIRE - RESIDENTAL ALARM",
      "ANML-BITE",  "MED - ANIMAL BITE",
      "ASSLT-IP",   "MED - ASSAULT IN PROGRESS",
      "ASSLT-RPT",  "MED - ASSAULT",
      "AST-AGEN",   "ASSIST OUTSIDE AGENCY",
      "AST-CIT",    "MED - CITIZEN ASSIST",
      "BOMB-THRT",  "FIRE - BOMB THREAT",
      "DIST-DV",    "MED - DOMESTIC ASSAULT",
      "DROWNING",   "MED - DROWNING",
      "EXPLSN",     "FIRE - EXPLOSION",
      "FIRE-ALRM",  "FIRE - FIRE ALARM",
      "FIRE-GRASS", "FIRE - GRASS/BRUSH FIRE",
      "FIRE-MISC",  "FIRE - UNK FIRE/INVESTIGATION",
      "FIRE-ODOR",  "FIRE - GAS/ODOR INVESTIGATION",
      "FIRE-P-ILL", "FIRE - ILLEGAL BURN",
      "FIRE-STRCT", "FIRE - STRUCTURE FIRE",
      "FIRE-VEH",   "FIRE - VEHICLE FIRE",
      "FIRE-WIRES", "FIRE - WIRES ARCHING",
      "HAZMAT",     "FIRE - HAZMAT / CHEMICAL SPILL",
      "MED-ALRM",   "MED - MEDICAL ALARM",
      "MED-ALS",    "ALS TRANSFER",
      "MED-AST",    "MED - MEDICAL ASSIST",
      "MED-BLS",    "BLS TRANSFER",
      "MED-CCT",    "CRITICAL CARE TRANSFER",
      "MED-EMRG",   "MED - MEDICAL EMERGENCY",
      "MED-TRAN",   "MED - MEDICAL TRANSPORT",
      "MENTAL",     "MED - PSYCHATRIC EMERGENCY",
      "MISS-ADLT",  "MISSING PERSON - ADULT",
      "MISS-CHLD",  "MISSING PERSON - CHILD",
      "OFF-DOWN",   "OFFICER DOWN",
      "SLIDE-OFF",  "MVA - VEHICLE SLIDE OFF",
      "SMOKE-IN",   "FIRE - INSIDE SMOKE INVESTIGATION",
      "SMOKE-OUT",  "FIRE - OUTSIDE SMOKE INVESTIGATION",
      "SUICIDAL",   "MED - SUICIDAL SUBJECT",
      "TEST-CALL",  "TEST CALL",
      "TRAF-HZRD",  "FIRE - TRAFFIC HAZZARD",
      "TRAF-STOP",  "TRAFFIC STOP",
      "TREES-DO",   "FIRE - TREE DOWN",
      "WATER-RSC",  "FIRE - WATER RESCUE",
      "WEL-CHK",    "MED - WELFARE CHECK",
      "WIRE-DOW",   "FIRE - WIRES DOWN"

  });
}
