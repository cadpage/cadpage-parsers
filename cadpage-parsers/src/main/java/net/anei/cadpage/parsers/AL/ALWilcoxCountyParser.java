package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALWilcoxCountyParser extends FieldProgramParser {

  public ALWilcoxCountyParser() {
    super("WILCOX COUNTY", "AL",
          "Nat:CALL! Add:ADDR! City:CITY! Date:DATETIME! LatLon:GPS! Comm:INFO! ");
  }

  @Override
  public String getFilter() {
    return "wilcox.al@ryzyliant.com,wilcox.al@ez911map.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Ryzy Alert")) return false;
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0, pt).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern TRAIL_DASH_PTN = Pattern.compile("[- ]+$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = TRAIL_DASH_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
}
