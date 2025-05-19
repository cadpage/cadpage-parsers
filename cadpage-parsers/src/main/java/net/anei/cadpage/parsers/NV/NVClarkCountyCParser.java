package net.anei.cadpage.parsers.NV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NVClarkCountyCParser extends FieldProgramParser {

  public NVClarkCountyCParser() {
    super("CLARK COUNTY", "NV",
          "ID! LOC:ADDR/S6! GRID:MAP! MAP_NEEDS:SKIP! COND:CALL! DEST:LINFO! CAUTION:ALERT! P_NAME:NAME! END");
  }

  private static final Pattern POST_MOVE_PTN = Pattern.compile("(POST MOVE)AMB:(\\S+)POST NAME: *(.*)");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("RUN# (\\d+) (DISP:.*)");
  private static final Pattern RR_BRK_PTN = Pattern.compile(" +(?=[A-Z]+:)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = POST_MOVE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL UNIT ADDR");
      data.strCall = match.group(1);
      data.strUnit = match.group(2);
      data.strAddress = match.group(3);
      return true;
    }

    match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = RR_BRK_PTN.matcher(match.group(2)).replaceAll("\n");
      return true;
    }

    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("RUN # *(.*)", true);
    return super.getField(name);
  }

}
