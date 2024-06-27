package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WIFondDuLacCountyParser extends DispatchA19Parser {

  public WIFondDuLacCountyParser() {
    super("FOND DU LAC COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "fdlco@fdlco.wi.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
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
}
