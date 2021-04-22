package net.anei.cadpage.parsers.ID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDBlaineCountyBParser extends FieldProgramParser {

  public IDBlaineCountyBParser() {
    super("BLAINE COUNTY", "ID",
          "( SELECT/1 CALL ADDRCITYST! X " +
          "| CODE CALL ADDRCITYST! X PLACE? INFO/N+? GPS1 GPS2 " + 
          ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "zdispatch@co.blaine.id.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MASTER1 = Pattern.compile(".*?[\n ]*\nBCEC: ([A-Z]{2}\\d{6}-\\d{3}) -- ");
  private static final Pattern MASTER2 = Pattern.compile(".*?\n(?:([A-Z]{2}\\d{6}-\\d{3})| )\n");
  @Override
  protected boolean parseMsg(String body , Data data) {

    Matcher match = MASTER1.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      data.strCallId =  match.group(1);
      body = body.substring(match.end()).trim();
      return parseFields(body.split("\n"), data);
    }

    match = MASTER2.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("2");
      data.strCallId = getOptGroup(match.group(1));
      body = body.substring(match.end()).trim();
      return parseFields(body.split("\n"), data);
    }
    return false;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PLACE")) return new PlaceField("[- A-Z]*", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}|None");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField(2)) return false;
      return super.checkParse(field, data);
    }
  }
}
