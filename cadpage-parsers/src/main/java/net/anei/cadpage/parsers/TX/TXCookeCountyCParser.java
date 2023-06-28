package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class TXCookeCountyCParser extends DispatchA78Parser {

  public TXCookeCountyCParser() {
    super("COOKE COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "donotreply@CookeCountySheriffsOfficealerts.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = convertCodes(data.strCity, CITY_CODES);
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "9",    "GAINESVILLE"
  });

}
