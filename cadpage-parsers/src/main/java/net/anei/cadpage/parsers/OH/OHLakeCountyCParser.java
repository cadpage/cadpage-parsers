package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class OHLakeCountyCParser extends DispatchA24Parser {
  
  public OHLakeCountyCParser() {
    super("LAKE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }

  private static final Pattern FIRENO_PTN = Pattern.compile("(.*) FireNo (\\d+)", Pattern.DOTALL);
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From: WillowickPager")) return false;
    String fireno = "";
    Matcher match = FIRENO_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      fireno = match.group(2);
    }
    if (!super.parseMsg(body, data)) return false;
    data.strCallId = append(data.strCallId, "/", fireno);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }
}
