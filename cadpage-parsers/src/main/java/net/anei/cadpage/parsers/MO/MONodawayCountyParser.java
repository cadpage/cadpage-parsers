package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MONodawayCountyParser extends DispatchBCParser {

  public MONodawayCountyParser() {
    super(CITY_LIST, "NODAWAY COUNTY", "MO", DispatchA33Parser.A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "JS239@OMNIGO.COM,JS239@MARYVILLEDPS.COM,NCAD@BC-EMS.COM,NODAWAYCOJAIL@GMAIL.COM,RUGBYFINN@GMAIL.COM,RUBYFINN@GMAIL.COM";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = data.strCity.replace(".", "");
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BARNARD",
      "BURLINGTON JUNCTION",
      "CLEARMONT",
      "CONCEPTION JUNCTION",
      "ELMO",
      "GRAHAM",
      "HOPKINS",
      "MARYVILLE",
      "PARNELL",
      "PICKERING",
      "RAVENWOOD",
      "SKIDMORE",
      "VILLAGES",
      "ARKOE",
      "CLYDE",
      "GUILFORD",

      // Census-designated place
      "CONCEPTION",

      // Other unincorporated places
      "ALLISON",
      "BEDISON",
      "BELL GROVE",
      "DAWSONVILLE",
      "GAYNOR",
      "ORRSBURG",
      "POSSUM WALK",
      "PUMPKIN CENTER",
      "QUITMAN",
      "ROSEBERRY",
      "WHITECLOUD",
      "WILCOX"
  };
}
