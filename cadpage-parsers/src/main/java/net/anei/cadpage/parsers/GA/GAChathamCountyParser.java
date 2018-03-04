package net.anei.cadpage.parsers.GA;

import java.util.regex.*;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Chatham County, GA
 */

public class GAChathamCountyParser extends FieldProgramParser {
  
  public GAChathamCountyParser() {
    super("CHATHAM COUNTY", "GA",
          "UNIT! RESPOND_TO:ADDR! APT:APT! ( C/C:INFO! Name:NAME! " +
                                          "| PRI:PRI! COMPLAINT:CALL! CRN:ID! Name:NAME ) COMMENTS:INFO");
  }

  private static final Pattern CRN_PATTERN
    = Pattern.compile("^((?:YOUR +)?CRN\\s*\\&\\s*TIMES(?: +ARE)?|"
+                         "COMPLETE/REFUSAL FOR|"
+                         "COMPLETED +CALL\\s*\\/\\s*REFUSAL TIMES|"
+                         "COMPLETE\\s*\\/\\s*REFUSAL)\\:(?:ADDRESS:)?([^:]*?)\\s*CRN\\:\\s*(\\d{4}(?:\\d|[A-Z])\\d{4}) +(.*)");
  private static final Pattern DELIM = Pattern.compile("(?<! ) *(?=(?:RESPOND TO|APT|C/C|PRI|COMPLAINT|CRN|Name|COMMENTS):)");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile("(?: +|(?<! |^))(?=(?:RECEIVED|DISPATCHED|ENROUTE|AT SCENE|DEPART|AT DEST|MILEAGE|CLEAR/CANC):)");
  private static final Pattern APT_PATTERN = Pattern.compile("(.*?)APT\\#?\\:?(.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher m = CRN_PATTERN.matcher(body);
    if (m.matches()) {
      setFieldList("CALL ADDR APT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = m.group(1);
      parseAddress(m.group(2).trim(), data);
      data.strCallId = m.group(3);
      data.strSupp = TIMES_BRK_PTN.matcher(m.group(4).trim()).replaceAll("\n");
      return true;
    }
    m = APT_PATTERN.matcher(body);
    if (m.matches()) {
      body = m.group(1)+" APT: "+m.group(2);
    } else {
      body = body.replace("PRI:", " APT: PRI:");
    }
    body = body.replace("C/O:", "COMPLAINT:");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Comment:");
      super.parse(field, data);
    }
  }
}
