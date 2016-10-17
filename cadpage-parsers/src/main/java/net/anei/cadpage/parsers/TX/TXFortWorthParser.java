package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
Fort Worth, TX

*/

public class TXFortWorthParser extends FieldProgramParser {

  public TXFortWorthParser() {
    super("FORT WORTH", "TX",
          "CALL_ALERT:CITY! Pri:PRI_CODE_CALL! Address:ADDR! APT:APT! Map:MAP! Cross:X!");
  }
  
  @Override
  public String getFilter() {
    return "CAD@MedStar911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Page")) return false;
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI_CODE_CALL")) return new MyPriCodeCallField();
    return super.getField(name);
  }
  
  private static final Pattern PRI_CODE_CALL_PTN = Pattern.compile("P(\\d) +(?:([^- ]*?)-)?(.*)");
  private class MyPriCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI_CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPriority = match.group(1);
      data.strCode = getOptGroup(match.group(2));
      data.strCall = match.group(3).trim();
    }

    @Override
    public String getFieldNames() {
      return "PRI CODE CALL";
    }
  }
}
