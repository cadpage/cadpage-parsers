package net.anei.cadpage.parsers.AR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARBentonCountyJParser extends FieldProgramParser {

  public ARBentonCountyJParser() {
    super("BENTON COUNTY", "AR",
          "ID CALL DATETIME ADDR ( GPS1 | CITY GPS1 | CITY ST/Z GPS1 ) GPS2! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no-reply@bentoncountyar.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CFS - Incident Code Changed - ") &&
        !subject.startsWith("CFS - Changed - ")) return false;
    return parseFields(body.split(","), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d", true);
    if (name.equals("ST")) return new StateField("([A-Z]{2})(?: +\\d{5})?");
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}|None");

  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }
}
