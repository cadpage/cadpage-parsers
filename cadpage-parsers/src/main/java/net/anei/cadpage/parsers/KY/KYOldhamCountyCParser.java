package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KYOldhamCountyCParser extends FieldProgramParser {

  public KYOldhamCountyCParser() {
    super("OLDHAM COUNTY", "KY",
          "( SELECT/NEW ADDRCITY ST GPS:GPS? | ADDR CITY ST_ZIP? ( GPS:GPS1 GPS2! | ) ) CALL CALL/CS+");
  }

  @Override
  public String getFilter() {
    return "dispatchfax@oldhamcountyky.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern INFO_MARK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - log -| *None$");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(.*) - Units: *(.*)");
  private static final Pattern LOG_DATE_TIME_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - log - *");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = TRAIL_UNIT_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strUnit = match.group(2).replace("; ", ",");
    }

    match = INFO_MARK_PTN.matcher(body);
    if (!match.find()) return false;
    String info = body.substring(match.start()).trim();
    body = body.substring(0, match.start()).trim();
    String[] flds = body.split("\\*");
    if (flds.length > 1) {
      setSelectValue("NEW");
    } else {
      flds = body.split(",");
      setSelectValue("OLD");
    }

    if (!parseFields(flds, data)) return false;

    if (!info.equals("None")) {
      for (String line : LOG_DATE_TIME_PTN.split(info)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO UNIT";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST_ZIP")) return new StateField("([A-Z]{2})(?: \\d{5})?", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " -");
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
