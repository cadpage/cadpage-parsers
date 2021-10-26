package net.anei.cadpage.parsers.WI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class WIFondDuLacCountyParser extends DispatchA41Parser {

  public WIFondDuLacCountyParser() {
    super(CITY_CODES, "FOND DU LAC COUNTY", "WI", "F[AFPS]|S[EFO]");
  }

  @Override
  public String getFilter() {
    return "cadmail@fdlco.wi.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CALLALERT:")) body = "DISPATCH:" + body.substring(10);
    if (!super.parseMsg(body, data)) return false;
    data.strSource = data.strChannel;
    data.strChannel = "";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CH", "SRC");
  }

  private static final Pattern DIR_NUMBER_ADDR = Pattern.compile("([NSEW])(\\d+ .*)");
  private String saveDir;

  @Override
  public String adjustMapAddress(String addr) {
    saveDir = null;
    Matcher match = DIR_NUMBER_ADDR.matcher(addr);
    if (match.matches()) {
      saveDir = match.group(1);
      addr = match.group(2);
    }
    return super.adjustMapAddress(addr);
  }

  @Override
  public String postAdjustMapAddress(String addr) {
    if (saveDir != null) addr = saveDir + addr;
    return super.postAdjustMapAddress(addr);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BRND", "BRANDON",
      "FAIR", "FAIRWATER",
      "FDL",  "FOND DU LAC",
      "OAK",  "OAKFIELD"
  });
}
