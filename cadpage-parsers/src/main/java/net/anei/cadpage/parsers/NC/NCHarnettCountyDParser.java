package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCHarnettCountyDParser extends FieldProgramParser {

  public NCHarnettCountyDParser() {
    super("HARNETT COUNTY", "NC",
          "PN:CALL! ADD:ADDR! CITY:CITY! TAC:CH! XST:X! INC#:ID LT/LNG:GPS/d UNIT(S):UNIT TIME:TIME CMT:INFO");
  }

  @Override
  public String getFilter() {
    return "cadpage@harnett.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("TIME:", " TIME:").replace("CMT:", " CMT:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static Pattern INFO_BRK_PTN = Pattern.compile(",? *\\[\\d{1,2}\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : INFO_BRK_PTN.split(field)) {
        part = part.trim();
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
}
