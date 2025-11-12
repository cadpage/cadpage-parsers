package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXUpshurCountyParser extends FieldProgramParser {

  public TXUpshurCountyParser() {
    super("UPSHUR COUNTY", "TX",
          "CALL GPS1 GPS2 ADDRCITYST! INFO/N+? ID DATETIME END");
  }

  @Override
  public String getFilter() {
    return "no-reply.zuercher@countyofupshur.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern DELIM = Pattern.compile(" *; +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "None ");
    if (body.endsWith(";")) body += ' ';
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("CFS\\d{3,}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
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
