package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

public class VAPatrickCountyBParser extends DispatchA76Parser {

  public VAPatrickCountyBParser() {
    super("PATRICK COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "cad@sheriff.co.patrick.va.us,tylerirvin93@gmail.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
//      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }

  private static final Pattern BAD_PIPE_PTN = Pattern.compile("\\b(?:APT|APARTMENT|UNIT)\\|");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("\nText STOP to opt out", "");
    int pt = body.indexOf("\n-- \n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = BAD_PIPE_PTN.matcher(body).replaceAll("");
    body = body.replace('\n',' ');
    if (subject.contains("SMSForwarder")) {
      if (body.length() >= 128 && body.length() <= 131) data.expectMore = true;
    }
    body = body.replace("| ", " | ");
    return super.parseMsg(body, data);
  }
}
