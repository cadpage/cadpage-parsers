package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
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
    return "dispatcher@safety-center.org,utcc@union-township.oh.us,dispatcher@lsfd.org,BrownCommCtr@BrownCountyOhio.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0)
      if (body.startsWith("Alert:")) {
        int pt = body.indexOf('\n');
        if (pt < 0) return false;
        subject = body.substring(0, pt).trim();
        body = body.substring(pt+1).trim();
      } else if (body.startsWith("LOC:")) {
        subject = "Alert:";
    }
    return super.parseMsg(subject, body, data);
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
