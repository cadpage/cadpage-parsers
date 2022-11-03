package net.anei.cadpage.parsers.CO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COWeldCountyBParser extends FieldProgramParser {

  public COWeldCountyBParser() {
    super("WELD COUNTY", "CO",
          "( EMPTY ADDR CITY PLACE UNIT CALL ID | ) INFO GPS1/d GPS2/d EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "777";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("WELD COUNTY: ")) return false;
    body = body.replace("WELD COUNTY: ", "");
    body = stripFieldStart(body, "Automated message from Dispatch:");

    int pt = body.indexOf("\nText STOP");
    if (pt >= 0) body = body.substring(0,pt).trim();

    return parseFields(body.split("\\|", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d\\d[A-Z]{3}\\d{6}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ,]*\\[\\d\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
