package net.anei.cadpage.parsers.general;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class GeneralAlertParser extends MsgParser {
  
  private static final Pattern[] EXTRA_BLANKS = new Pattern[]{
    Pattern.compile(" +(\n)"),
    Pattern.compile("(   ) +")
  };
  
  public GeneralAlertParser() {
    super("", "");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.msgType = MsgType.GEN_ALERT;
    for (Pattern ptn : EXTRA_BLANKS) {
      body = ptn.matcher(body).replaceAll("$1");
    }
    if (subject.length() > 0) {
      body = "(" + subject + ") " + body;
    }
    data.strSupp = body;
    return true;
  }
}
