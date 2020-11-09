package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Washington County, TN
 */
public class TNWashingtonCountyParser extends FieldProgramParser {

  public TNWashingtonCountyParser() {
    super("WASHINGTON COUNTY", "TN",
          "CALL UNIT2? ADDR PLACE_X/Z+? MAP_TIME! GPS1/d GPS2/d END");
  }

  @Override
  public String getFilter() {
    return "JCFDTEXT@johnsoncitytn.org,CAD@wc911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern HWY_11E_PTN = Pattern.compile("(HIGHWAY|HWY|US) +11 E\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern BRK_TIME_PTN = Pattern.compile(" \\d\\d?:\\d\\d:\\d\\d\\b");
  private static final String EXTRA = "Think green: ";
  private static final Pattern MASTER = Pattern.compile("(?:([A-Z]+)(?:\\.[^-]+)?-)?(.*?) +(?:AT|TO) +(.*?) (?:-X-ST:(.*?) +)?(?:MAP-([-,A-Z0-9 ]+?) +)?(\\d\\d:\\d\\d)");
  private static final Pattern SIGNAL_PTN = Pattern.compile("(.*?) +SIGNAL \\d");

  @Override
  protected boolean parseMsg(String body, Data data) {

    int pt = body.lastIndexOf('\n');
    if (pt >= 0) {
      String tail = body.substring(pt+1).trim();
      if (tail.startsWith(EXTRA) || EXTRA.startsWith(tail)) {
        body = body.substring(0,pt).trim();
      }
    }

    body = body.replace("(-1)", "");
    body = HWY_11E_PTN.matcher(body).replaceAll("$1 11E");

    String tBody = body;
    if (tBody.contains("\n")) {
      pt = tBody.indexOf("\nXstr ");
      if (pt < 0) pt = tBody.indexOf("\nCross Streets ");
      if (pt > 0) {
        Matcher match = BRK_TIME_PTN.matcher(tBody);
        if (match.find(pt+5)) {
          tBody = tBody.substring(0,match.start()) + '\n' + tBody.substring(match.start()+1);
        }
      }

      String[] flds = tBody.split("\n");
      if (flds.length >= 3) {
        return parseFields(flds, data);
      }
    }

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    setFieldList("UNIT CALL ADDR APT PLACE X MAP TIME");

    data.strUnit = getOptGroup(match.group(1));
    data.strCall = match.group(2).trim();
    String addr = match.group(3).trim();
    data.strCross = getOptGroup(match.group(4)).replaceAll(" *- *", " & ");
    data.strMap = getOptGroup(match.group(5));
    data.strTime = match.group(6);

    match = SIGNAL_PTN.matcher(addr);
    if (match.matches()) addr = match.group(1);
    pt = addr.indexOf(';');
    if (pt >= 0) {
      parseAddress(addr.substring(0,pt).trim(), data);
      data.strPlace = addr.substring(pt+1).trim();
    } else {
      parseAddress(StartType.START_ADDR, addr, data);
      data.strPlace = getLeft();
    }
    if (data.strApt.equals("0")) data.strApt = "";

    return true;
  }

  @Override
  public Field getField(String  name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT2")) return new Unit2Field();
    if (name.equals("PLACE_X")) return new MyPlaceCrossField();
    if (name.equals("MAP_TIME")) return new MyMapTimeField();
    return super.getField(name);
  }

  private static final Pattern CALL_PTN = Pattern.compile("(.*?)(?: ((?: ?\\b(?:[A-Z]{1,3}FD|[A-Z]+[0-9]+|\\d{3}|AGENCY|R10LVFD)\\b)+(?:,[,A-Z0-9]+)?))?(?: \\[([,0-9]+)\\])?");
  private class MyCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strUnit = getOptGroup(match.group(2)).replace(' ', ',');
        data.strUnit = append(data.strUnit, ",", getOptGroup(match.group(3)));
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT";
    }
  }

  // UNIT2 Lists units if there wern'e included in the CPU field
  private static final Pattern UNIT_PTN = Pattern.compile("(?:[A-Z]+[0-9]+|AGENCY|[A-Z]{1,3}FD)(?:,[,A-Z0-9]+)?");
  private class Unit2Field extends UnitField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strUnit.length() > 0) return false;
      Matcher match = UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = field;
      return true;
    }
  }

  private static final Pattern X_TAG_PTN = Pattern.compile("X-STR= |Xstr |Cross Streets ");
  private static final Pattern GPS_PTN = Pattern.compile("\\d\\d\\.\\d{6,}/-\\d\\d\\.\\d{6,}");
  private class MyPlaceCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      boolean cross = data.strCross.length() > 0;
      Matcher match = X_TAG_PTN.matcher(field);
      if (match.lookingAt()) {
        cross = true;
        field = field.substring(match.end()).trim();
      }
      if (cross) {
        int pt = field.indexOf(';');
        if (pt == 0) {
          field = field.substring(1).trim();
          if (field.equals("DEAD END")) {
            super.parse(field, data);
          } else {
            data.strPlace = append(data.strPlace, " - ", field);
          }
        } else {
          if (pt > 0) field = field.substring(0, pt).trim();
          super.parse(field, data);
        }
      } else if (GPS_PTN.matcher(field).matches()) {
         setGPSLoc(field, data);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE GPS X";
    }
  }

  private static final Pattern MAP_TIME_PTN = Pattern.compile("(?:Map ([-A-Za-z0-9,/ ]+) )?(\\d\\d?:\\d\\d:\\d\\d)(?: Case)?(?: (\\d*))?(?: Rcvd.*)?");
  private static final Pattern MAP_TIME_TRUNC_PTN = Pattern.compile("Map ([^:]+)(?: [0-9:]+)?");
  private class MyMapTimeField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_TIME_PTN.matcher(field);
      if (match.matches()) {
        data.strMap = getOptGroup(match.group(1));
        data.strTime = match.group(2);
        data.strCallId = append(data.strCallId, "/", getOptGroup(match.group(3)));
        return true;
      }

      if (field.equals("Map")) return true;
      match = MAP_TIME_TRUNC_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strMap = match.group(1).trim();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "MAP TIME ID";
    }

  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return sAddress.replace("OLD STATE ROUTE", "OLD_STATE_ROUTE");
  }

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return sAddress.replace("OLD_STATE_ROUTE", "OLD STATE ROUTE");
  }

}
