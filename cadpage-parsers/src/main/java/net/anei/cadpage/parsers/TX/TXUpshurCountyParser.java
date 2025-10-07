package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXUpshurCountyParser extends FieldProgramParser {

  public TXUpshurCountyParser() {
    super("UPSHUR COUNTY", "TX",
          "CALL GPS1 GPS2 ADDRCITYST! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no-reply.zuercher@countyofupshur.com";
  }

  private static final Pattern DELIM = Pattern.compile(" *; +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.endsWith(";")) body += ' ';
    String[] flds = DELIM.split(body);
    if (!subject.equals(flds[0])) return false;
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b[- ]*(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
