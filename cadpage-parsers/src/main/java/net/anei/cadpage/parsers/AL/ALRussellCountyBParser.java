package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class ALRussellCountyBParser extends MsgParser {

  public ALRussellCountyBParser() {
    super("RUSSELL COUNTY", "AL");
    setFieldList("SRC CALL ADDR APT CITY INFO");
  }

  private static final Pattern TRAIL_MARK = Pattern.compile("\n\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4} - ");
  private static final Pattern MASTER = Pattern.compile("(.*?)@(.*?)\\*(.*?)\\|(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith(":")) return false;
    data.strSource = subject.substring(1).trim();

    String tailInfo = "";
    Matcher match = TRAIL_MARK.matcher(body);
    if (match.find()) {
      tailInfo = body.substring(match.start()+1);
      body = body.substring(0,match.start()).trim();
    }

    body = body.replace('\n', ' ');
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    data.strCity = match.group(3).trim().replace(".", "");
    data.strSupp = append(match.group(4).trim(), "\n", tailInfo);
    return true;
  }
}
