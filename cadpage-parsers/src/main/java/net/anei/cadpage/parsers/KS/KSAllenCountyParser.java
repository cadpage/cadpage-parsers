package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSAllenCountyParser extends DispatchA25Parser {
  
  public KSAllenCountyParser() {
    super("ALLEN COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "reports@allencounty911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strAddress = fixAddress(data.strAddress);
    return true;
  }
  
  private static final Pattern NUMBER_ST_AV_PTN = Pattern.compile("(\\d+) (AVE?|ST)\\b", Pattern.CASE_INSENSITIVE);
  private String fixAddress(String addr) {
    Matcher match = NUMBER_ST_AV_PTN.matcher(addr);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        String street = match.group(1);
        char lastDigit = street.charAt(street.length()-1);
        String suffix = lastDigit == '1' ? "st" : lastDigit == '2' ? "nd" : lastDigit == '3' ? "rd" : "th";
        match.appendReplacement(sb, street+suffix+' '+match.group(2));
      } while (match.find());
      match.appendTail(sb);
      addr = sb.toString();
    }
    return addr;
  }
  
  
}
