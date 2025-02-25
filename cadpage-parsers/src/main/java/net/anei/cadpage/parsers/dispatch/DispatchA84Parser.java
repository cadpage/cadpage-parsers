package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA84Parser extends FieldProgramParser {

  public DispatchA84Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA84Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "NOTIFYTYPE:SKIP! CALL:CODE_CALL! ADDR:ADDRCITY! CROSSSTREETS:X! ID:ID! PRI:PRI! DATE:DATETIME! MAP:SKIP! UNIT:UNIT! INFO:INFO/N+ DISTRICT:MAP! GROUP:MAP/D! AREA:MAP/D! LAT:GPS1! LON:GPS2 END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strCode =  field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("<") && field.endsWith(">")) return;
      field = stripFieldStart(field, "btwn ");
      field = field.replaceAll(" and ", "/");
      super.parse(field, data);
    }
  }

  private static final Pattern DATETIME_PTN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATETIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }
}
