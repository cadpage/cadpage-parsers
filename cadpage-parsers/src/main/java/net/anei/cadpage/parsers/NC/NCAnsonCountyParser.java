
package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCAnsonCountyParser extends DispatchSouthernParser {

  public NCAnsonCountyParser() {
    super(CITY_LIST, "ANSON COUNTY", "NC", 0);
  }
  
  @Override
  public String getFilter() {
    return "chyatt@ansonvillefire.com,notifyuser@co.anson.nc.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("notifyuser:")) return false;
    body = body.substring(11).trim();
    if (! super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("0 ")) data.strAddress = data.strAddress.substring(2).trim();
    return true;
  }
  
  @Override
  protected void parseExtra(String sExtra, Data data) {
    Matcher match = EXTRA_PTN.matcher(sExtra);
    if (match.find()) {
      data.strSupp = sExtra.substring(match.end()).trim();
      sExtra = sExtra.substring(0,match.start()).trim();
    }
    data.strCall = sExtra;
  }
  private static final Pattern EXTRA_PTN = Pattern.compile(" (?=MA ?\\d:)");

  private static final String[] CITY_LIST = new String[]{
    "ANSONVILLE",
    "LILESVILLE",
    "MCFARLAN",
    "MORVEN",
    "NORTH ANSONVILLE",
    "PEACHLAND",
    "POLKTON",
    "WADESBORO"
  };
}
