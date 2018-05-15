package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;




public class PAArmstrongCountyBParser extends DispatchA9Parser {
  
  public PAArmstrongCountyBParser() {
    super(CITY_CODES, "ARMSTRONG COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,911@co.armstrong.pa.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "LEECHBURG BORO",    "LEECHBURG",
      "KISKI TWP",         "KISKIMINETAS TWP",
      "KITTG BORO",        "KITTANNING",
      "KITTG TWP",         "KITTANNING TWP"
  });
}
