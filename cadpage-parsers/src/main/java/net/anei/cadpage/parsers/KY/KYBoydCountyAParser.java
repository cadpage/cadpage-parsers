package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYBoydCountyAParser extends DispatchB2Parser {
  
  public KYBoydCountyAParser() {
    super("BC911:", KYBoydCountyBParser.CITY_LIST, "BOYD COUNTY", "KY", B2_FORCE_CALL_CODE);
    setupCallList(KYBoydCountyBParser.CALL_LIST);
    setupSpecialStreets(KYBoydCountyBParser.SPECIAL_STREET_LIST);
    removeWords("ALLEY");
    setupMultiWordStreets(KYBoydCountyBParser.MWORD_STREET_LIST);
  }
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    int pt = field.indexOf("* ");
    if (pt >= 0) {
      field = field.substring(0,pt)+" @ "+field.substring(pt+2);
    }
    field = KYBoydCountyBParser.fixAddress(field).toUpperCase();
    if (!super.parseAddrField(field, data)) return false;
    data.strApt = stripFieldEnd(data.strApt, " BLDG");
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    city = convertCodes(city, KYBoydCountyBParser.CITY_TABLE);
    return super.adjustMapCity(city);
  }
}
