package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAWalkerCountyBParser extends FieldProgramParser {

  public GAWalkerCountyBParser() {
    super("WALKER COUNTY", "GA",
          "CALL CALL_EXT/CS? ADDR/S6 CITY X X/ZCS+? PLACE UNIT/Z GPS1 GPS2 PHONE INFO/CS+? ID EMPTY END");
  }

  @Override
  public String getFilter() {
    return "communications@walkerga.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Information")) return false;
    body = body.replace(" Quadrant:", ", Quadrant:");
    if (body.endsWith(",")) {
      setSelectValue("1");
      body += ' ';
    } else {
      setSelectValue("2");
    }
    return parseFields(body.split(", ", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_EXT")) return new CallField("Leak or Odor");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9,]*", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ID")) return new IdField("#?(\\d{4}-\\d+)(?: \\([A-Z]*\\d+\\))?", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      data.strCross = append(data.strCross, ", ", field);
    }
  }

  private static final Pattern GPS1_PTN = Pattern.compile("[-+]?\\d{2}.\\d+");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
      setPattern(GPS1_PTN);
    }
  }
}
