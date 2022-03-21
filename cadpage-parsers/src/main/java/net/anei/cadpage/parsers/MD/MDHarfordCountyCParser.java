package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MDHarfordCountyCParser extends DispatchA48Parser {

  public MDHarfordCountyCParser() {
    super(MDHarfordCountyParser.CITY_LIST, "HARFORD COUNTY", "MD", FieldType.PLACE, A48_NO_CODE);
  }

  @Override
  public String getFilter() {
    return "@harfordcountymd.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strPlace.contains("/ -")) {
      data.strCross = data.strPlace.replace("/ -", "/");
      data.strPlace = "";
    }
    return true;
  }

}
