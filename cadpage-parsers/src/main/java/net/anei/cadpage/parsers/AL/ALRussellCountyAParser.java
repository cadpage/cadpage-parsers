package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class ALRussellCountyAParser extends DispatchA19Parser {
  
  public ALRussellCountyAParser() {
    super("RUSSELL COUNTY", "AL");
  }
  
  private static final Pattern MSPACE_PTN = Pattern.compile(" {5,}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Russell County Dispatch")) return false;
    subject = subject.substring(23).trim();
    if (subject.startsWith("Incident #:")) body = MSPACE_PTN.matcher(subject).replaceAll("\n") + '\n' + body;
    return super.parseMsg("",  body, data);
  }
  
  @Override
  public String getFilter() {
    return "leecountyal@gmail.com,ripnrun@rusco911.com,account@ooma.com";
  }
}
