package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXCyCreekCommCenterBParser extends FieldProgramParser {
  
  public TXCyCreekCommCenterBParser() {
    super("HARRIS COUNTY", "TX", 
          "ADDR:ADDR! APT:APT! PLACE:PLACE! X-ST:X! MAP:MAP! SUB:CITY! NATURE:CALL! PRI:PRI! UNITS:UNIT! LAT:GPS1! LON:GPS2! ID:ID! ( NOTES:INFO | CN:INFO )");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" CN - ", " CN: ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)-(\\S.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
