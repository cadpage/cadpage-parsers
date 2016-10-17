package net.anei.cadpage.parsers.KY;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class KYHardinCountyAParser extends DispatchH02Parser {
  
  public KYHardinCountyAParser() {
    super(CITY_CODES, "HARDIN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "NO-REPLY@HCKY.ORG";
  }

  private static final Pattern ADDR_JUNK_PTN = Pattern.compile(" *\\(S\\) *\\(N\\)[A-Z ]+");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    // One oddball transitional call 
    if (subject.startsWith("Incident:")) {
      subject = "Dispatch Report Incident #" + subject.substring(9).trim();
    }
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strAddress = ADDR_JUNK_PTN.matcher(data.strAddress).replaceAll("");
    data.strAddress = stripFieldEnd(data.strAddress, "()");
    return true;
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BIG",  "BIG CLIFTY",
      "CECI", "CECILIA",
      "EAST", "EASTVIEW",
      "ELIZ", "ELIZABETHTOWN",
      "FORT", "FORT KNOX",
      "GARF", "GARFIELD",
      "GLEN", "GLENDALE",
      "HODG", "HODGENVILLE",
      "LEBA", "LEBANON JUNCTION",
      "LRCO", "LARUE CO",
      "MULD", "MULDRAUGH",
      "NEWH", "NEW HAVEN",
      "RADC", "RADCLIFF",
      "RINE", "RINEYVILLE",
      "SONO", "SONORA",
      "UPTO", "UPTON",
      "VINE", "VINE GROVE",
      "WEST", "WEST POINT",
      "WHIT", "WHITE MILLS"

  });
}
