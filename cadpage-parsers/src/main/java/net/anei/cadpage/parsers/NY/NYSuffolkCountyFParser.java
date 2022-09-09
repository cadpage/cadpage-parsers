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
    super(CITY_LIST, "SUFFOLK COUNTY", "NY",
          "CALL! ( TOA:TOA! CODE? ADDR/S6Xa! CS:X? NAME? ( IDP! | CODE IDP! ) INFO/N+ " +
                "| PLACE ADDR/ZS6Xa CITY! TOA:TOA! SRC ID_INFO! INFO/N+? HQ UNIT/S+ " + 
                "| ADDR/ZS6Xa CITY! TOA:TOA! SRC ID_INFO! INFO/N+? HQ UNIT/S+ " + 
                "| PLACE ADDR/ZS6! CS:X? TOA:TOA! ID_INFO! INFO/N+ " +
                ")");
  }
  
  @Override
  public String getFilter() {
    return "@firerescuesystems.xohost.com,masticambco@optonline.net,paging@wadingriverfd.info,amityvillefdpaging1@gmail.com,amityvillefdpaging@gmail.com,paging@amityvillefireinfo.com,paging@frspaging.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    data.strSource = subject;
    
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
  public String getProgram() {
    return "SRC? " + super.getProgram();
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
  
  private static final String[] CITY_LIST = new String[]{
    "AMITYVILLE",
    "MASSAPEQUA",
    "W BABYLON"
  };
}
	