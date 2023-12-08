package net.anei.cadpage.parsers.FL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class FLPolkCountyParser extends DispatchA41Parser {

  public FLPolkCountyParser() {
    super(CITY_CODES, "POLK COUNTY", "FL", "WEST|TC\\d+");
  }

  @Override
  public String getFilter() {
    return "@polksheriff.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\n", "");
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FTM", "FORT MEADE",
      "FTU", "FORT MEADE"
  });

}
