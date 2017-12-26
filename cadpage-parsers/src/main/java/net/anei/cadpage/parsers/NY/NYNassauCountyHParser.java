package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNassauCountyHParser extends NYSuffolkCountyXBaseParser {
  
  public NYNassauCountyHParser() {
    super("NASSAU COUNTY", "NY",
           "CALL PLACENAME ADDR/SXa! CS:X? TOA:TOAP IDP INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Rule out alerts intended for NYNassauCountyFireTracker
    if (subject.equals("FirePage")) return false;
    
    return super.parseMsg(body, data);
  }

  @Override
  public String getFilter() {
    return "scmbackup@verizon.net,cpg.page@gmail.com,wbpaging@optonline.net,paging2@firerescuesystems.xohost.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_DASH_PTN = Pattern.compile("(\\d+) - (\\d+ .*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_DASH_PTN.matcher(field);
      if (match.matches()) field = match.group(1)+'-'+match.group(2);
      super.parse(field,  data);
    }
  }
}
