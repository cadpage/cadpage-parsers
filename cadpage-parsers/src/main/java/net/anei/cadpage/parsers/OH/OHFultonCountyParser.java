package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHFultonCountyParser extends FieldProgramParser {

  public OHFultonCountyParser() {
    super("FULTON COUNTY", "OH",
          "CALL:CALL! PLACE:SKIP! ADDR:ADDR/S6! CITY:CITY! LAT:GPS1! LONG:GPS2! AREA:MAP! PRI:PRI! TIME:TIME! UNIT:UNIT? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d?:\\d\\d?", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern UNIT_TIME_PTN = Pattern.compile("UNIT \\S+ \\| .*");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("COMMENT: Terminal \\S+ \\| +");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (UNIT_TIME_PTN.matcher(field).matches()) return;
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      if (field.startsWith("Call Initiated by ")) return;
      super.parse(field, data);
    }
  }
}
