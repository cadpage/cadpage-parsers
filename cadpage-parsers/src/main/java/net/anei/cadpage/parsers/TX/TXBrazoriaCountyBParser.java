package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;


public class TXBrazoriaCountyBParser extends DispatchA55Parser {
  public TXBrazoriaCountyBParser() {
    super("BRAZORIA COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = AVE_X_PTN.matcher(addr).replaceAll("AVENUE $1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern AVE_X_PTN = Pattern.compile("\\bAVE ([A-Z])\\b");
}
