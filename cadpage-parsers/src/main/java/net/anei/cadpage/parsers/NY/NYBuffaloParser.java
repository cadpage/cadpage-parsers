package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class NYBuffaloParser extends DispatchProQAParser {
  
  public NYBuffaloParser() {
    super("BUFFALO", "NY",
           "PRI CODE_CALL CALL/L+? ADDR/Z CITY ALT_ID/Z TIME! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "2002000004,777";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("PRI")) return new PriorityField("(\\d) .*", true);
    if (name.equals("ALT_ID")) return new SkipField("\\d+", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\d?[A-Z]\\d\\d?[A-Z]?) +(.*)");
  private class MyCodeCallField extends Field {
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
