package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TXCyCreekCommCenterBParser extends FieldProgramParser {

  public TXCyCreekCommCenterBParser() {
    this("HARRIS COUNTY", "TX");
  }

  public TXCyCreekCommCenterBParser(String defCity, String defState) {
    super(defCity, defState,
          "ADDR:ADDR! APT:APT! PLACE:PLACE! X-ST:X! MAP:MAP! SUB:CITY! NATURE:CALL! PRI:PRI! UNITS:UNIT! LAT:GPS1! LON:GPS2! ID:ID! ( NOTES:INFO | CN:INFO )");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern RR_PTN = Pattern.compile("ID:(\\d\\d-\\d+) +(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = RR_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).replace(' ', '\n');
      return true;
    }
    body = body.replace(" CN - ", " CN: ");
    if (!super.parseMsg(body, data)) return false;
    data.strAddress = data.strAddress.replace("KINGSCOTE DR", "KINGSCOATE DR");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}(?:[A-E]\\d{1,2}[A-Z]?)?)[- ]+(\\S.*)|([A-Z0-9]+)-(\\S.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        String code = match.group(1);
        field = match.group(2);
        if (code == null) {
          code = match.group(3);
          field = match.group(4);
        }
        data.strCode = code;
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
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
