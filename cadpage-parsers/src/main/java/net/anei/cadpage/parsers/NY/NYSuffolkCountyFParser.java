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
           "CALL! ( TOA:TOA! CODE? ADDR/S6Xa! CS:X? NAME? ( IDP! | CODE IDP! ) INFO+ | PLACE ADDR/S6Xa CITY! TOA:TOA! SRC ID! INFO+? HQ UNIT/S+ )");
  }
  
  @Override
  public String getFilter() {
    return "@firerescuesystems.xohost.com,masticambco@optonline.net,paging@wadingriverfd.info,amityvillefdpaging1@gmail.com,amityvillefdpaging@gmail.com,paging@amityvillefireinfo.com";
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
  public Field getField(String name) {
    if (name.equals("HQ")) return new SkipField("HQ");
    return super.getField(name);
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
	