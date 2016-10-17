package net.anei.cadpage.parsers.MD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MDCecilCountyBParser extends FieldProgramParser {
  
  private static final Pattern NORT_PTN = Pattern.compile("\\bNORT\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");

  public MDCecilCountyBParser() {
    super("CECIL COUNTY", "MD",
           "CODE XTRA? CALL ADDR/S0P X/Z+? TIME ID INFO+");
  }

  @Override
  public String getFilter() {
    return "singerly@gmail.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = NORT_PTN.matcher(body).replaceAll("NORTH");
    if (! parseFields(body.split("\n"), data)) return false;
    if (data.strCode.equals("OOC")) data.defCity = "";
    return true;
  }
  
  private class MyTimeField extends TimeField {
    public MyTimeField() {
      setPattern(TIME_PTN, true);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (TIME_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("\\d{4}[A-Z]?|OOC", true);
    if (name.equals("XTRA")) return new SkipField(".{3,6}");
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
