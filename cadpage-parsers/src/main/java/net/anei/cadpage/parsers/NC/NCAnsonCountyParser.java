
package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCAnsonCountyParser extends DispatchSouthernParser {

  public NCAnsonCountyParser() {
    super(CITY_LIST, "ANSON COUNTY", "NC", 
          DSFLG_ADDR | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME);
    setCallPtn("[-A-Z]+");
  }
  
  private static final Pattern MARKER = Pattern.compile("notifyuser:|911:");
  
  @Override
  public String getFilter() {
    return "chyatt@ansonvillefire.com,notifyuser@co.anson.nc.us,911@co.anson.nc.us,9911@ansoncountync.gov";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end()).trim();
    if (! super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("0 ")) data.strAddress = data.strAddress.substring(2).trim();
    return true;
  }
  
  private static final Pattern AT_PTN = Pattern.compile("\\bAT\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected void parseMain(String sAddr, Data data) {
    sAddr = AT_PTN.matcher(sAddr).replaceAll("@");
    super.parseMain(sAddr, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "ANSONVILLE",
    "LILESVILLE",
    "MCFARLAN",
    "MORVEN",
    "NORTH ANSONVILLE",
    "PEACHLAND",
    "POLKTON",
    "WADESBORO",
    "STANLY"
  };
}
