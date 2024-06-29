package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJBurlingtonCountyIParser extends FieldProgramParser {

  public NJBurlingtonCountyIParser() {
    super("BURLINGTON COUNTY", "NJ",
          "INCIDENT:ID! TITLE:CALL! PLACE:PLACE? ADDRESS:ADDR? CROSSSTREET:X? GPS:GPS! BOX:BOX? LEVEL:PRI! NOTES:INFO! INFO/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +[,.][ ,.]* ");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
