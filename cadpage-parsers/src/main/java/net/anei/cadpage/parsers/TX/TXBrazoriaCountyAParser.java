package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;


public class TXBrazoriaCountyAParser extends DispatchA22Parser {
  
  public TXBrazoriaCountyAParser() {
    super(CITY_CODES, "BRAZORIA COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "page@manvelpd.org";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return BACKWARD_HWY_PTN.matcher(addr).replaceAll("TX $1");
  }
  private static final Pattern BACKWARD_HWY_PTN = Pattern.compile("\\b(\\d+) *SH", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BC",  "BRAZORIA COUNTY"
  });
}
