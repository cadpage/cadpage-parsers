package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NYAlbanyCountyDParser extends FieldProgramParser {

  public NYAlbanyCountyDParser() {
    super("ALBANY COUNTY", "NY",
          "CALL EMPTY UNIT EMPTY EMPTY? ADDR EMPTY! Cross_streets:X! Routing_Info:SKIP! Business:PLACE! RP:NAME! PHONE:PHONE! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "root@capsnet.albany-ny.org,@colonie.org>";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" PHONE:", "\nPHONE:");
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) field = field.substring(0,pt).trim();
      data.strName = field;
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("-*");
  private static final Pattern INFO_ID_TIME_PTN = Pattern.compile("Seq: (\\d+) TO: ([^ ]*?) ST: ([^ ]*) ID: *(.*)");
  private static final Pattern INFO_RR_TIME_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d [A-Z0-9]+ +.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (INFO_JUNK_PTN.matcher(field).matches()) return;

      Matcher match = INFO_ID_TIME_PTN.matcher(field);

      if (match.matches()) {
        data.strCallId = append(match.group(1), "/", match.group(4));
        String time = match.group(2);
        if (!time.equals("EVNT")) data.strTime = time;
        field = "ST: " + match.group(3);
      }

      if (INFO_RR_TIME_PTN.matcher(field).matches()) {
        if (data.msgType == MsgType.PAGE){
          data.msgType = MsgType.RUN_REPORT;
          data.strSupp = field;
        } else {
          data.strSupp = append(data.strSupp, "\n", field);
        }
      } else if (data.msgType == MsgType.PAGE) {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "ID TIME " + super.getFieldNames();
    }
  }
}
