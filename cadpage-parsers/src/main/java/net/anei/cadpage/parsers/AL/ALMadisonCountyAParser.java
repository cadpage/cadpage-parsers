package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Madison County, AL
 */

public class ALMadisonCountyAParser extends FieldProgramParser {

  private static final String CAD_MARKER = "IPS I/Page Notification";

  public ALMadisonCountyAParser() {
    this("MADISON COUNTY", "AL");
  }

  ALMadisonCountyAParser(String defCity, String defState) {
    super(ALMadisonCountyBParser.CITY_CODES, defCity, defState,
           "EVENT:ID? Loc:ADDR! Mun:CITY Xstreets:X? EVT#:ID TYPE:CALL TIME:TIME% GRID_ID:MAP%");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "Madco911,rescue1-bounces@rescuesquad.net,cad.page@madco9-1-1.org,cad.page@hsv.madco911.com,pageout@hsv.madco911.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 150; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  private static final Pattern TRUNC_MAP_PTN = Pattern.compile("\\d{1,2}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    body = stripFieldStart(body, "/ ");
    do {
      if (subject.contains(CAD_MARKER)) break;

      if (body.startsWith(CAD_MARKER)) {
        body = body.substring(CAD_MARKER.length()).trim();
        body = stripFieldStart(body, "/ ");
        break;
      }

      if (body.startsWith("Loc:")) break;

      return false;
    } while (false);

    body = body.replace("GRID_ID:", "GRID ID:");
    String info = null;
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      info = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
    }
    if (!super.parseMsg(body, data)) return false;

    // Address and call ID are both optional.  But one of them
    // must be present
    if (data.strAddress.length() == 0 &&  data.strCallId.length() == 0) return false;
    if (!data.expectMore) {
      data.expectMore = TRUNC_MAP_PTN.matcher(data.strMap).matches();
    }

    if (info != null) {
      boolean isUnit = false;
      for (String term : info.split("\n")) {
        term = term.trim();
        if (term.length() == 0) continue;
        if (!isUnit) {
          if (term.equalsIgnoreCase("[Remark]")) continue;
          if (term.equalsIgnoreCase("[Unit]")) {
            isUnit = true;
          }
          else if (term.startsWith("Problem:")) {
            data.strCall = append(data.strCall, " - ", term.substring(8).trim());
          }
          else if (data.strCode.length() == 0 && term.startsWith("Dispatch CAD Code:")) {
            data.strCode = term.substring(18).trim();
          }
          else {
            data.strSupp = append(data.strSupp, "\n", term);
          }
        } else {
          data.strUnit = append(data.strUnit, ",", term);
        }
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " CALL? CODE? INFO UNIT";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) *(.*)");
  private static final Pattern COLON_PTN = Pattern.compile(" *: *");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("null", "");
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      field = field.replaceAll(" alias ", " @");
      if (field.startsWith("@")) {
        data.strAddress = COLON_PTN.matcher(field.substring(1)).replaceAll(" ").trim();
      } else {
        Parser p = new Parser(field);
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, p.get(":"), data);
        while (true) {
          String term = p.get(':');
          if (term.length() == 0) break;
          Matcher match = APT_PTN.matcher(term);
          if (match.matches()) {
            data.strApt = append(data.strApt, "-", match.group(1));
          } else {
            term = stripFieldStart(term, "@");
            if (!term.equals("PSI")) {
              data.strPlace = append(data.strPlace, " - ", term);
            }
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = convertCodes(field, TYPE_CODES);
    }
  }

  private static final Pattern PRIV_PTN = Pattern.compile("\\bPRIV\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = PRIV_PTN.matcher(addr).replaceAll("").trim();
    return super.adjustMapAddress(addr);
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "2029 FLAGSTONE DR",                    "+34.692179,-86.720033",
      "148 GARDENGATE DR",                    "+34.836230,-86.699971",
      "2150 HUGHES RD",                       "+34.758041,-86.741481",
      "100 KENWORTH CT",                      "+34.733952,-86.756533",
      "102 KENWORTH CT",                      "+34.734207,-86.756528",
      "104 KENWORTH CT",                      "+34.734444,-86.756519",
      "106 KENWORTH CT",                      "+34.734686,-86.756533",
      "107 KENWORTH CT",                      "+34.734743,-86.757148",
      "108 KENWORTH CT",                      "+34.734888,-86.756513",
      "109 KENWORTH CT",                      "+34.734961,-86.757184",
      "110 KENWORTH CT",                      "+34.735112,-86.756542",
      "111 KENWORTH CT",                      "+34.735200,-86.757172",
      "112 KENWORTH CT",                      "+34.735297,-86.756521",
      "113 KENWORTH CT",                      "+34.735461,-86.757175",
      "114 KENWORTH CT",                      "+34.735513,-86.756531",
      "115 KENWORTH CT",                      "+34.735713,-86.757196",
      "116 KENWORTH CT",                      "+34.735718,-86.756503",
      "117 KENWORTH CT",                      "+34.735870,-86.756995",
      "118 KENWORTH CT",                      "+34.735882,-86.756690"
  });

  private static final Properties TYPE_CODES = buildCodeTable(new String[]{
      "UNKMED",         "UNKNOWN MEDICAL",
      "M",              "MEDICAL",
      "FR",             "FIRE/RESCUE",
      "MVA/I",          "MVA W/ INJURY",
      "F/ALARMS",       "FIRE ALARM",
      "ALM/M",          "MEDICAL ALARM",
      "MVA/E",          "MVA W/ ENTRAPMENT",
      "F/OUTSIDE_FIRE", "OUTSIDE FIRE",
      "F/STRUCTURE",    "STRUCTURE FIRE"
  });
}
