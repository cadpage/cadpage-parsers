package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class MSPontotocCountyParser extends MsgParser {

  public MSPontotocCountyParser() {
    super("PONTOTOC COUNTY", "MS");
    setFieldList("CALL ADDR APT DATE TIME");
  }

  @Override
  public String getFilter() {
    return "noreply@emergencycallworx.com,4693809763";
  }

  private static final Pattern MASTER = Pattern.compile("at (.*) (\\d\\d-\\d\\d-\\d\\d) (\\d\\d:\\d\\d)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    for (String part : subject.split("\\|")) {
      part = part.trim();
      part = stripFieldStart(part, "(");
      part = stripFieldEnd(part, ")");
      data.strCall = append(data.strCall, " - ", part);
    }
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strDate = match.group(2).replace('-', '/');
    data.strTime = match.group(3);
    return true;
  }

}
