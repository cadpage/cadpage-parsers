package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOBentonCountyParser extends DispatchBCParser {
  public MOBentonCountyParser() {
    super("BENTON COUNTY", "MO", DispatchA33Parser.A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "911.ADMIN@BENTONCOMO.COM,notifications@bentoncomo.com";
  }

  private static final Pattern HIGHWAY_XX_PTN = Pattern.compile("\\b(?:HIGHWAY|HWY) ([A-Z]{1,2})\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String postAdjustMapAddress(String address) {
    Matcher match = HIGHWAY_XX_PTN.matcher(address);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        String hwy = match.group(1);
        if (hwy.length() == 2 && hwy.charAt(0) != hwy.charAt(1)) {
          hwy = match.group();
        } else {
          hwy = "STATE HWY " + hwy;
        }
        match.appendReplacement(sb, hwy);
      } while (match.find());
      address = sb.toString();
    }
    return address;
  }
}
