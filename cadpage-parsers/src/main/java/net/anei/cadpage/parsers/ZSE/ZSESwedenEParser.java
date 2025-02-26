package net.anei.cadpage.parsers.ZSE;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenEParser extends ZSESwedenBaseParser {

  public ZSESwedenEParser() {
    super("", "",
        "Adress:ADDR! " +
        "Adressbeskriv.:PLACE " +
        "Fritext:INFO/N! " +
        "Namn:PLACE! " +
        "Notering:INFO/N " +
        "Nyckel:MAP " +
        "Objektinfo:INFO " +
        "Objektskort:URL " +
        "Plats:MAP " +
        "Position_WGS84:GPS! " +
        "Presentationsgrupp:CALL! " +
        "Samhälle:CITY " +
        "Talgrupp:CH!",
        FLDPROG_ANY_ORDER);
  }

  @Override
  public String getLocName() {
    return "Contal";
  }

  @Override
  public String getFilter() {
    return "Larmserver@kallareetuna.se,noreply.larm@nerikesbrandkar.se";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  protected boolean parseMsg(String body, Data data) {
    int ndx = body.indexOf(":");
    if (ndx < 2) return false;
    if (!body.substring(ndx-1, ndx+2).equals(" : ")) return false;
    body = cleanFixedLabelBreaks(body, ndx);
    if (body == null) return false;
    return parseFields(body.split("\n"), data);
  }
}
