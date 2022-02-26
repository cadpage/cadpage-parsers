package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLPutnamCountyParser extends FieldProgramParser {

  public FLPutnamCountyParser() {
    super("PUTNAM COUNTY", "FL",
          "( Incident:ID! Station:UNIT! Complaint:CODE_CALL! Address_Street:ADDR/S Cross_Street:X! Place:PLACE! Latitude:GPS1! Longitude:GPS2! Map:SKIP! " +
          "| CODE_CALL ADDR APT ID UNIT! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "@putnamsheriff.org,dispatch@pertdispatch.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("=")) data.strCall = subject;
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    String[] flds = body.split("\n");
    if (flds.length >= 8) {
      return parseFields(flds, data);
    } else {
      return parseFields(flds[0].trim().split(";",-1), data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ID")) return new IdField("[A-Z]+[A-Z0-9]+\\d", true);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+) - +(.*)");
  private class MyCodeCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      if (data.strCall.isEmpty()) data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
