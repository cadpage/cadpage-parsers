package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXTarrantCountyCParser extends DispatchA18Parser {

  public TXTarrantCountyCParser() {
    super(TXTarrantCountyParser.CITY_LIST, "TARRANT COUNTY","TX");
  }

  @Override
  public String getFilter() {
    return "crimes@cityofkeller.com,crimes@benbrook-tx.gov,active911@sansompark.org,crimespage@lakeworthtx.org,cad@evermantx.net,cad@evermanfire.org,cadeverman@gmail.com,CRIMES@EULESSTX.GOV";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = body.indexOf("\n\nCONFIDENTIALITY");
    if (pt >= 0) body = body.substring(0, pt).trim();
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("NONE")) data.strCity = "";
    return true;
  }
}
