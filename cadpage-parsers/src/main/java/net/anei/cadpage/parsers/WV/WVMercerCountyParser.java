package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVMercerCountyParser extends DispatchA19Parser {

  public WVMercerCountyParser() {
    super(CITY_CODES, "MERCER COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "dispatch@mercer911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strUnit = stripFieldStart(data.strUnit, "CANCEL,");
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
//      "BLU", "BLUEWELL",

      "ATH", "ATHENS",
      "BEE", "BEESON",
      "BLF", "BLUEFIELD",
      "BLU", "BLUEFIELD",
      "BRM", "BRAMWELL",
      "CAM", "CAMP CREEK",
      "COU", "MERCER COUNTY",
      "FLT", "FLAT TOP",
      "GAR", "GARDNER",
      "GRV", "GREEN VALLEY",
      "HER", "HERNDON",
      "HIA", "HIAWATHA",
      "HWA", "HIAWATHA",
      "JUM", "JUMPING BRANCH",
      "KEG", "KEGLEY",
      "LER", "LERONA",
      "LSH", "LASHMEET",
      "MAY", "MAYBEURY",
      "MNT", "MONTCALM",
      "MTK", "MATOAKA",
      "OAK", "OAKVALE",
      "ODD", "ODD",
      "PIP", "PIPESTEM",
      "PRI", "PRINCETON",
      "PRN", "PRINCETON",
      "RCK", "ROCK",
      "SPN", "SPANISHBURG",
      "WLF", "WOLFE"

  });

}
