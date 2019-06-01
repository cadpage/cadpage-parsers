package net.anei.cadpage.parsers.ZSE;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenEParser extends ZSESwedenBaseParser {
  
  public ZSESwedenEParser() {
    super("", "",
        "Presentationsgrupp:CALL! Talgrupp:CH! Objektinfo:INFO! INFO/N+ Notering:INFO/N! INFO/N+ Fritext:INFO/N! INFO/N+ Namn:PLACE! INFO/N+ Adress:ADDR! Adressbeskriv.:PLACE/N Plats:MAP! Samhälle:CITY! Position_WGS84:GPS! END"); 
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
    body = cleanFixedLabelBreaks(body, 19);
    if (body == null) return false;
    return parseFields(body.split("\n"), data);
  }
}
