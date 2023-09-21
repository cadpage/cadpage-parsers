package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAJeffersonCountyCParser extends FieldProgramParser {

  public WAJeffersonCountyCParser() {
    this("JEFFERSON COUNTY", "WA");
  }

  WAJeffersonCountyCParser(String defCity, String defState) {
    super(defCity, defState,
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID_CODE! PRI:PRI! DATE:DATETIME! ( QUADRANT:MAP DISSTRICT:MAP/L | ) MAP:MAP/L! UNIT:UNIT! INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us,Dispatch@clallamcountywa.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_CODE")) return new MyIdCodeField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }


  private static final Pattern ID_CODE_PTN = Pattern.compile("(.*?) *\\((.*)\\)");
  private class MyIdCodeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);

        // Code isn't valid right now :(
//        data.strCode = match.group(2).trim();
      }
      data.strCallId = field;
    }

    @Override
    public String getFieldNames() {
      return "ID CODE";
    }
  }
}
