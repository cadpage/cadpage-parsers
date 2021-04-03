package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * Harnett County, NC
 *
 * Replaces NCHarnetCountyA
 */
public class NCHarnettCountyCParser extends FieldProgramParser {

  String select;

  public NCHarnettCountyCParser() {
    super(NCHarnettCountyParser.CITY_LIST, "HARNETT COUNTY", "NC",
           "( SELECT/DASHFMT ID ADDR APT+? CITY/Y X/Z+? EMPTY EMPTY+? CALL EMPTY+? UNIT " +
           "| CITY/Y X X CH EMPTY CALL! EMPTY EMPTY EMPTY UNIT% INFO+ " +
           "| ID ADDR EMPTY+? CITY/Y X/Z+? CALL UNIT! END " +
           "| X/Z X/Z CH EMPTY EMPTY? CALL! EMPTY EMPTY EMPTY UNIT% INFO+ )");

  }

  @Override
  public String getFilter() {
    return "cadpage@harnett.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Text Message / ");
    setSelectValue("");
    String[] flds = body.split("\n");
    if (flds.length == 1) {
      setSelectValue("DASHFMT");;
      if (body.endsWith("-")) body += ' ';
      flds = body.split(" - ");
    }
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    if (name.equals("CALL"))  return new MyCallField();
    if (name.equals("CH")) return new ChannelField("TAC\\d+|");
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?");
  private class MyCallField extends CallField {

    @Override
    public void parse(String field, Data data) {
      if (CALL_CODE_PTN.matcher(field).matches()) {
        data.strCode = field;
        String call = CALL_CODES.getCodeDescription(field, true);
        if (call == null) call = field;
        data.strCall = call;
      }
      else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static CodeTable CALL_CODES = new StandardCodeTable();
}
