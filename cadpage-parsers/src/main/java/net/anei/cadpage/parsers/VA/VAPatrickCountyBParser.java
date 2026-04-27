package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
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
  private static final Pattern ZIP_APT_PTN = Pattern.compile("(\\d{5})\\b[, ]*(.*)");
  private static final Pattern BAD_CITY_PTN = Pattern.compile("(?!\\d{5}$).*\\d.*");

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
    if (!super.parseMsg(body, data)) return false;
    Matcher match = ZIP_APT_PTN.matcher(data.strCity);
    if (match.matches()) {
      data.strCity = match.group(1);
      data.strApt = append(data.strApt, ", ", match.group(2));
    } else if (BAD_CITY_PTN.matcher(data.strCity).matches()) {
      data.strApt = append(data.strApt, ", ", data.strCity);
      data.strCity = "";
    }
    return true;
  }
}
