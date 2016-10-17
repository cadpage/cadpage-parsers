package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKSandSpringsParser extends FieldProgramParser {
  
  public OKSandSpringsParser() {
    super("SAND SPRINGS", "OK", 
          "CALL:CALL! ADDR:ADDR! ID:ID! DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT! INFO:INFO! INFO/N+ DIRECTIONS:INFO/N! INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(":ADDR: ", "\nADDR:");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{4}) +(.*)");
  private class MyCallField extends Field {

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
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("DIRECTIONS:") || field.equals("WARNINGS:")) return;
      if (field.startsWith("DIRECTIONS:") || field.startsWith("WARNINGS:")) field = '\n' + field;
      super.parse(field, data);
    }
  }

}
