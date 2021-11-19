package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILKaneCountyFParser extends FieldProgramParser {

  public ILKaneCountyFParser() {
    super("KANE COUNTY", "IL",
          "DATETIME! Fire_Call:CALL! Police_Call:CALL! UNIT! EMPTY! ADDRCITY! ID! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatch@quadcom911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\w{2,5}) - (.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      String code = match.group(1);
      String call = match.group(2);
      if (call.startsWith("Assist") && !data.strCall.startsWith("Assist")) return;
      if (data.strCall.isEmpty() || data.strCall.startsWith("Assist") && !call.startsWith("Assist")) {
        data.strCode = code;
        data.strCall = call;
      } else {
        if (!code.equals(data.strCode)) data.strCode = append(data.strCode, "/", code);
        if (!call.equals(data.strCall))data.strCall = append(data.strCall, " / ", call);
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
