package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MOPettisCountyBParser extends DispatchH05Parser {
  
  public MOPettisCountyBParser() {
    super("PETTIS COUNTY", "MO", 
          "CALL:CODE_CALL! PLACE:PLACE! ADDR:ADDRCITY/S6! XST:X! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY! INFO_BLK+ UNIT_STATUS_TIMES:EMPTY! TIMES+ CALLER:NAME! CALLER_PHONE:PHONE!");
  }

  @Override
  public String getFilter() {
    return "donotreply@cityofsedalia.com";
  }
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n-- \n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME"))  return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d+) +(.*)");
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
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = null;
      String[] parts = field.split(",");
      switch (parts.length) {
        case 3:
          apt = parts[2].trim();
          
        case 2:
          data.strCity = parts[1].trim();
          
        case 1:
          super.parse(parts[0].trim(), data);
          if (apt != null && !apt.equals(data.strApt)) {
            data.strApt = append(data.strApt, "-", apt);
          }
          return;
          
          default:
            abort();
      }
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}
