package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class ILCookCountyFParser extends DispatchA27Parser {
  
  public ILCookCountyFParser() {
    super("COOK COUNTY", "IL", "[A-Z]+\\d*");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org,noreply@everbridge.net";
  }
  
  private static final Pattern PHONE_INFO_PTN = Pattern.compile("(\\(\\d{3}\\)\\d{3}-\\d{4})\\b *");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    Matcher match = PHONE_INFO_PTN.matcher(data.strSupp);
    if (match.lookingAt()) {
      data.strPhone = match.group(1);
      data.strSupp = data.strSupp.substring(match.end());
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("INFO", "PHONE INFO");
  }
}
