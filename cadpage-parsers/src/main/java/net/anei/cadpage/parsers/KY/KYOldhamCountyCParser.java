package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class KYOldhamCountyCParser extends FieldProgramParser {

  public KYOldhamCountyCParser() {
    super("OLDHAM COUNTY", "KY",
          "ADDR CITY ST_ZIP CALL! END");
  }

  @Override
  public String getFilter() {
    return "dispatchfax@oldhamcountyky.gov";
  }

  private static final Pattern INFO_MARK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - log -| None$");
  private static final Pattern LOG_DATE_TIME_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - log - *");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = INFO_MARK_PTN.matcher(body);
    if (!match.find()) return false;
    String info = body.substring(match.start()).trim();
    body = body.substring(0, match.start()).trim();
    parseFields(body.split(","), data);
    if (!info.equals("None")) {
      for (String line : LOG_DATE_TIME_PTN.split(info)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST_ZIP")) return new StateField("([A-Z]{2}) \\d{5}", true);
    return super.getField(name);
  }

}
