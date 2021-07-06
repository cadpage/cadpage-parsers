package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA80Parser;

/**
 * Parke County, IN
 */
public class INParkeCountyParser extends DispatchA80Parser {

  public INParkeCountyParser() {
    super("PARKE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "parke911dispatch@gmail.com,ParkeCountyDispatch@outlook.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    if (!body.startsWith("DISPATCH:")) body = "DISPATCH:" + body;
    return super.parseMsg(body, data);
  }
}
