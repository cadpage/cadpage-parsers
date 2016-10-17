package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHLovelandParser extends DispatchA1Parser {
  
  public OHLovelandParser() {
    this("LOVELAND", "OH");
  }
  
  protected OHLovelandParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState);
    addExtendedDirections();
  }
  
  @Override
  public String getAliasCode() {
    return "OHLoveland";
  }
  
  @Override
  public String getFilter() {
    return "dispatcher@safety-center.org,utcc@union-township.oh.us,dispatcher@lsfd.org";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return TR_PTN.matcher(addr).replaceAll("TERRACE");
  }
  private static final Pattern TR_PTN = Pattern.compile("\\bTR\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "LOVELD", "LOVELAND",
      "SYMMTP", "SYMMES TWP"
  });
}
