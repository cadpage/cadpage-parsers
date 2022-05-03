package net.anei.cadpage.parsers.MD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDQueenAnnesCountyCParser extends FieldProgramParser {

  public MDQueenAnnesCountyCParser() {
    super("QUEEN ANNES COUNTY", "MD",
          "CALL ADDRCITY PLACE X UNIT ( Radio_Channel:CH! | CH! ) ( Box_Area:MAP GPS1 GPS2! | ) INFO! DATETIME END");
  }

  @Override
  public String getFilter() {
    return "Dispatch@qac.gov,@c-msg.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!") && !subject.equals("CMalert")) return false;
    return parseFields(body.split(" \\| "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d?:\\d\\d:\\d\\d");
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}|-361|");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }
}
