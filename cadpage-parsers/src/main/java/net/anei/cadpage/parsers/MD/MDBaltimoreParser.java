package net.anei.cadpage.parsers.MD;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MDBaltimoreParser extends FieldProgramParser {

  public MDBaltimoreParser() {
    super(CITY_CODES, "BALTIMORE", "MD",
          "( DISPATCH CODE_CALL! Location:ADDRCITY! X1! Box_Area:MAP! ( Unit:UNIT! | Units:UNIT! ) Call_Entry_Comments:INFO! ProQA_Comments:INFO/N CAD_Case_No:ID! END " +
          "| ADDR CITY MAP CALL UNIT2/C+? UNIT_X X+? EMPTY+? ID! END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "cad.paging@baltimorecity.gov";
  }

  private static final Pattern MASTER = Pattern.compile("(.*)(?: - |\n)From:? [A-Z0-9]+ (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)", Pattern.DOTALL);

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    data.strDate = match.group(2);
    data.strTime = match.group(3);

    //  There are two formats, one line brk deliminted and one comma delimited
    if (body.startsWith("| DISPATCH |")) {
      return parseFields(body.split("\n"), data);
    }
    String[] flds = split(body);
    if (flds.length >= 5) return parseFields(flds, data);

    // Otherwise report as general alert
    setFieldList("INFO");
    data.msgType = MsgType.GEN_ALERT;
    data.strSupp = body;
    return true;
  }

  private String[] split(String body) {
    List<String> flds = new ArrayList<String>();
    int st = 0;
    int parenCnt = 0;
    for (int ndx = 0; ndx<body.length(); ndx++) {
      char chr = body.charAt(ndx);
      switch (chr) {
      case '(':
        parenCnt++;
        break;
      case ')':
        parenCnt--;
        break;
      case ',':
        if (parenCnt == 0) {
          flds.add(body.substring(st, ndx));
          st = ndx+1;
        }
        break;
      }
    }
    flds.add(body.substring(st));
    return flds.toArray(new String[flds.size()]);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DISPATCH")) return new SkipField("\\| DISPATCH \\|", true);
    if (name.equals("CODE_CALL")) return new CodeCallField();
    if (name.equals("X1")) return new CrossField("btwn +(.*)", true);
    if (name.equals("MAP")) return new MapField("[A-Z]?\\d{1,2}-\\d{1,2}", true);
    if (name.equals("UNIT2")) return new UnitField("[A-Z0-9]+", true);
    if (name.equals("UNIT_X")) return new MyUnitCrossField();
    if (name.equals("ID")) return new IdField("[A-Z]{2}\\d{8}");
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class CodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+3).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }

  }

  private static final Pattern UNIT_X_PTN = Pattern.compile("([A-Z0-9]+) *(\\(.*\\))");
  private class MyUnitCrossField extends MyCrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_X_PTN.matcher(field);
      if (!match.matches()) return false;
      String unit = match.group(1);
      String cross = match.group(2).trim();
      if (!super.checkParse(cross, data)) return false;
      data.strUnit = append(data.strUnit, ",", unit);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT " + super.getFieldNames();
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("(") || !field.endsWith(")")) return false;
      field = field.substring(1, field.length()-1).trim();
      if (field.startsWith("<") && field.endsWith(">")) return true;

      if (field.startsWith("at ")) {
        field = field.substring(3).trim();
        data.strPlace = data.strAddress;
        data.strAddress = "";
        int pt = field.indexOf(',');
        if (pt >= 0) {
          data.strCity = convertCodes(field.substring(pt+1).trim(), CITY_CODES);
          field = field.substring(0,pt);
        }
        parseAddress(field, data);
        return true;
      }

      data.strCross = append(data.strCross, " / ", stripFieldStart(field, "btwn "));
      return true;
    }

    @Override
    public String getFieldNames() {
      return "X PLACE ADDR APT CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAL", "BALTIMORE"
  });

}
