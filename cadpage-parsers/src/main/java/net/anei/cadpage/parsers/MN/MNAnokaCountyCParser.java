package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNAnokaCountyCParser extends FieldProgramParser {

  public MNAnokaCountyCParser() {
    super("ANOKA COUNTY", "MN",
          "( FIRE/RESCUE_PAGE%EMPTY! ID CALL ADDR APT PLACE CITY NAME! INFO/N+? MAP GPS/Zd! END " +
          "| Active_911%EMPTY! ID CALL ADDR APT PLACE CITY NOT_FOUND%EMPTY! INFO/N+? NAME PHONE/Z GPS/Zd! END )");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARKER")) return new SkipField("FIRE/RESCUE PAGE|Active 911", true);
    if (name.equals("ID")) return new IdField("[A-Z]{0,3}\\d{7,8}", true);
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("GPS")) return new GPSField("\\d{8} +\\d{8}", true);
    return super.getField(name);
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NOT FOUND")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[\\d{1,2}\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        line = stripFieldEnd(line.trim(), ",");
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      super.parse(field, data);
    }
  }
}
