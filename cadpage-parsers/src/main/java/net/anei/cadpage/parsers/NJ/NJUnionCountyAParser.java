package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NJUnionCountyAParser extends FieldProgramParser {

  public NJUnionCountyAParser() {
    super("UNION COUNTY", "NJ",
          "Unit:UNIT! Problem_Nature:CALL! City:CITY! Address:ADDR! Loc_Name:PLACE! Blg:APT! Apt:APT! .ID_#:ID! END");
  }

  @Override
  public String getFilter() {
    return "ucdispatch@ucnjps.org";
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Unit:(\\S+) +Units CC ?#(\\d{4}-\\d{6}) +(Disp.*?)[ .]*Master Incident C*#: *(\\d{4}-\\d{6})");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d)(?=[A-Z])");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = match.group(1);
      data.strCallId = match.group(2)+'/'+match.group(4);
      data.strSupp = TIMES_BRK_PTN.matcher(match.group(3).trim()).replaceAll("\n");
      return true;
    }
    else {
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("\\.? *(.*)", true);
    return super.getField(name);
  }
}
