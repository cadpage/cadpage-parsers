package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class TXHaysCountyDParser extends DispatchA57Parser {

  public TXHaysCountyDParser() {
    super("HAYS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "cadpage@co.hays.tx.us,Alert@active911.com,@hpcapplications.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;

    if (data.strApt.contains("FRONTAGE RD")) {
      data.strAddress = append(data.strAddress, " ", data.strApt);
      data.strApt = "";
    }
    return true;
  }

}
