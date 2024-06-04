package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TNClaiborneCountyParser extends FieldProgramParser {

  public TNClaiborneCountyParser() {
    super("CLAIBORNE COUNTY", "TN",
          "CFS:ID! CALL! ADDRCITYST! ( GPS! | X_PLACE GPS! | ) ( X_PLACE UNIT_TIMES! | UNIT_TIMES | PHONE? NAME FAIL Received:TIMES! ) TIMES/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" \\|{2}", -1), data);
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
      if (field.contains(" / ")) {
        data.strCross = append(data.strCross, " / ", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      // TODO Auto-generated method stub
      return null;
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
