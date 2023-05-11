package net.anei.cadpage.parsers.NV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class NVClarkCountyDParser extends FieldProgramParser {

  public NVClarkCountyDParser() {
    super("CLARK COUNTY", "NV",
          "Unit:UNIT! Inc:ID! Pri:PRI! Map:MAP? Prob:CALL! Add:ADDR! Apt:APT Loc:PLACE Name:NAME");
  }

  @Override
  public String getFilter() {
    return "alert@epageit.net,sms@pageway.net,44627545,7027738326";
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Unit:(\\S+) *(?:Inc: *(\\S+) )?(Rec:.*)");
  private static final Pattern RUN_REPORT_BRK_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d|:) *(?=(?:Enr|Arr|Avail|Cx):)");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Inc:|Pri:|Map:|Prob:|Add:|Apt:|Loc:|Name:)");
  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "SMS / ");
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = match.group(1);
      data.strCallId = getOptGroup(match.group(2));
      data.strSupp = RUN_REPORT_BRK_PTN.matcher(match.group(3)).replaceAll("\n");
      return true;
    }

    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)-(.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
