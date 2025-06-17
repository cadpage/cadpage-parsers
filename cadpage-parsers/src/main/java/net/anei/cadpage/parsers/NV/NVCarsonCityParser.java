package net.anei.cadpage.parsers.NV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NVCarsonCityParser extends FieldProgramParser {

  public NVCarsonCityParser() {
    super("CARSON CITY", "NV",
          "DESCRIPTION:CALL! GPS:GPS! DETAILS:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@775ofr.com";
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("#([-A-Za-z]+)(.*)");
  private static final Pattern ID_PTN = Pattern.compile("\\d{3,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strAddress = stripFieldEnd(match.group(1), "-");
    match = ID_PTN.matcher(match.group(2));
    if (match.find()) data.strCallId = match.group();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ADDR ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("%2C", ",");
      super.parse(field, data);
    }
  }
}
