package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ORPolkCountyParser extends FieldProgramParser {
  
  public ORPolkCountyParser() {
    super("POLK COUNTY", "OR",
          "CALL SRC DATETIME ADDR/SP UNIT ID! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "wvccsupdesk@cityofsalem.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), 6, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{3}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("[A-Z]{3}\\d{12}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern INFO_PHONE_PTN = Pattern.compile("\\[(\\d{3}-\\d{3}-\\d{4})\\] *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PHONE_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE " + super.getFieldNames();
    }
  }
}
