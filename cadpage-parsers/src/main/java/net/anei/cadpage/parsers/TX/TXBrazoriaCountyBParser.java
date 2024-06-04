package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class TXBrazoriaCountyBParser extends DispatchBCParser {
  public TXBrazoriaCountyBParser() {
    super("BRAZORIA COUNTY", "TX");
    setupMultiWordStreets("STEPHEN F AUSTIN");
  }

  @Override
  public String getFilter() {
    return "FREEPORTDISPATCH@FREEPORT.TX.US,no-reply@freeport.tx.us,noreply@omnigo.com";
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = AVE_X_PTN.matcher(addr).replaceAll("AVENUE $1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern AVE_X_PTN = Pattern.compile("\\bAVE ([A-Z])\\b");
}
