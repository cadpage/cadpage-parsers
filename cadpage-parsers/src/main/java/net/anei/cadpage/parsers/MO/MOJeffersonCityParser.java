package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJeffersonCityParser extends FieldProgramParser {

  public MOJeffersonCityParser() {
    super("JEFFERSON CITY", "MO",
          "CALL UNIT ( ADDR_CITY_X_PLACE/S6 X2? | ADDR/S6! X ) INFO/N+");
    setupSaintNames("LOUIS");
    removeWords("X");
  }

  @Override
  public String getFilter() {
    return "paging@jeffcitymo.org,paging@jeffersoncitymo.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_DIRO;
  }

  private static final Pattern TRAIL_ID_PTN = Pattern.compile(" +Incident # -(?: (\\d{4}-\\d{8})(?: *\\(\\d+\\))?)?$");
  private static final Pattern TRAIL_GPS_PTN = Pattern.compile(" +(?:GPS: *)?[-+]?\\d{2,3}\\.\\d+ ++[-+]?\\d{2,3}\\.\\d+$");
  private static final Pattern TRAIL_OPERATOR_PTN = Pattern.compile(" +[a-z]+$");
  private static final Pattern TRAIL_TIME_PTN = Pattern.compile(" +(\\d\\d:\\d\\d)$");

  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {

    if (!subject.equals("DONOTREPLY")) return false;

    Matcher match = TRAIL_ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = getOptGroup(match.group(1));
      body = body.substring(0,match.start());
    }

    match = TRAIL_GPS_PTN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(), data);
      body = body.substring(0,match.start());
    }

    match = TRAIL_OPERATOR_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());

    match = TRAIL_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      body = body.substring(0,match.start());
    }

    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " TIME GPS ID";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_CITY_X_PLACE")) return new MyAddressCityCrossPlaceField();
    if (name.equals("X2")) return new MyCross2Field();
    return super.getField(name);
  }

  private static final Pattern ADDR_X_PLACE_PTN = Pattern.compile("(.*?) / (.*?) -(?: (.*))?");
  private class MyAddressCityCrossPlaceField extends AddressCityField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ADDR_X_PLACE_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1).trim(), data);
      data.strCross = match.group(2).trim();
      data.strPlace = getOptGroup(match.group(3));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X PLACE";
    }
  }

  private class MyCross2Field extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCross.length() > 0) return false;
      if (field.contains(" / ")) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
}
