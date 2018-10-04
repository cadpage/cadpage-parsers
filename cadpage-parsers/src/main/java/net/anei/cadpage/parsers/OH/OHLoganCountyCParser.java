package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class OHLoganCountyCParser extends FieldProgramParser {
  
  public OHLoganCountyCParser() {
    super("LOGAN COUNTY", "OH",
          "DATETIME CALL! ADDR CITY INFO END");
  }
  
  @Override
  public String getFilter() {
    return "smarlow@ci.bellefontaine.oh.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Dispatched")) return false;
    
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    
    return parseFields(body.split(", ", 5), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d");
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class  MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt < 0) abort();
      data.strCode = field.substring(0, pt);
      data.strCall = field.substring(pt+1).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        int pt = line.indexOf(": ");
        if (pt >= 0) line = line.substring(pt+2).trim();
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
