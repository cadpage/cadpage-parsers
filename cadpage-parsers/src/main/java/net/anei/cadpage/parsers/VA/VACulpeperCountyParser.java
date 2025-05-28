package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VACulpeperCountyParser extends FieldProgramParser {

  public VACulpeperCountyParser() {
    super("CULPEPER COUNTY", "VA",
           "NONE? ADDRCITYST PLACE ( GPS1 GPS2! | ) CALL DATETIME INFO EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "CAD@culpepercounty.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("RESPOND (CFS\\d+)");
  private static final Pattern DELIM = Pattern.compile(" *\\| +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);

    return parseFields(DELIM.split(body+' ', -1), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("NONE")) return new SkipField("None", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}|None");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
    }
  }
}
