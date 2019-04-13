package net.anei.cadpage.parsers.ZSE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenDParser extends ZSESwedenBaseParser {
  
  public ZSESwedenDParser() {
    super("", "", 
          "Namn:CODE_CALL! Objektinfo:ADDR! Mapp:MAP? Nyckel:SKIP! Fritext:INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{3}) +(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
