package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Suffolk County (variant F)
 */
public class NYSuffolkCountyFParser extends NYSuffolkCountyXBaseParser {
  
  private static final Pattern PREFIX_PTN = Pattern.compile("([^\\*\n]+)(\\*\\*.*)", Pattern.DOTALL);

  public NYSuffolkCountyFParser() {
    super("SUFFOLK COUNTY", "NY",
           "CALL! TOA:TOA! CODE? ADDR/S6Xa! CS:X? NAME? ( IDP! | CODE IDP! ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@firerescuesystems.xohost.com,masticambco@optonline.net,paging@wadingriverfd.info";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    String prefix = null;
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.matches()) {
      prefix = match.group(1).trim();
      body = match.group(2);
    }
    if (!super.parseMsg(body, data)) return false;
    if (prefix != null) {
      data.strCall = data.strCall + " (" + prefix + ")";
    }
    return true;
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.equals("CTY")) return true;
    return false;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return stripFieldEnd(addr, " CTY");
  }
}
	