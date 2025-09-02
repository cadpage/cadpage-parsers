package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TNClaiborneCountyParser extends FieldProgramParser {

  public TNClaiborneCountyParser() {
    super("CLAIBORNE COUNTY", "TN",
          "CFS:ID! CALL! ADDRCITYST! ( GPS! | X_PLACE GPS! | ) ( X_PLACE UNIT_TIMES! | UNIT_TIMES! | X_PLACE PHONE! NAME FAIL Received:TIMES! | PHONE? NAME FAIL Received:TIMES! ) TIMES/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("iSOMS - ")) return false;
    subject = subject.substring(8).trim();
    if (subject.startsWith("CAD UNIT ")) {
      subject = subject.substring(9).trim();
      int pt  = subject.indexOf(' ');
      if (pt < 0) pt = subject.length();
      data.strSource = subject.substring(0,pt);
      subject = subject.substring(pt).trim();
      if (subject.equals("COMPLETED-CALL")) data.msgType = MsgType.RUN_REPORT;
    }
    return parseFields(body.split("\n", -1), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT_TIMES")) return new SkipField("Unit Times", true);
    if (name.equals("X_PLACE")) return new MyCrossPlaceField();
    if (name.equals("GPS")) return new GPSField("\\d+\\.\\d+, -\\d+\\.\\d+", true);
    if (name.equals("PHONE")) return new PhoneField("\\d{10}", true);
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private class MyCrossPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("@")) {
        data.strCross = append(data.strCross, " / ", field.substring(1).trim());
      }
      else if (field.contains(" / ")) {
        data.strCross = append(data.strCross, " / ", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }

  }

  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = getRelativeField(0);
      data.strSupp = append(data.strSupp, "\n", field);
      if (field.startsWith("Completed:") || field.startsWith("Canceled:")) data.msgType = MsgType.RUN_REPORT;
    }
  }
}
