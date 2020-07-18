package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Harrison County, WV
 */
public class WVHarrisonCountyAParser extends FieldProgramParser {

  public WVHarrisonCountyAParser() {
    super("HARRISON COUNTY", "WV",
          "DATETIME! LOC:ADDRCITYST! INC:CALL! GPS1 GPS2 UNIT! END");
  }

  @Override
  public String getFilter() {
    return "zuercher@harrisoncountywv.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern ID_PTN = Pattern.compile("CFS\\d+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! ID_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;
    if (body.endsWith(";")) body += ' ';
    return parseFields(body.split("; ", -1), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
