package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

public class ORMarionCountyCParser extends MsgParser {

  public ORMarionCountyCParser() {
    super("MARION COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "noreply@everbridge.net";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!body.startsWith("<!doctype html>"))  return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.msgType = MsgType.GEN_ALERT;
    data.strCall = subject;
    int pt1 = body.indexOf("<!-- Messages start-->");
    if (pt1 < 0) return false;
    pt1 += 22;
    int pt2 = body.indexOf("<!-- Messages end-->", pt1);
    if (pt2 < 0) return false;
    body = body.substring(pt1, pt2);
    for (String line : body.split("\n")) {
      data.strSupp = append(data.strSupp, "\n", line.trim());
    }
    return true;
  }


}
