package net.anei.cadpage.parsers.ZSE;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenBParser extends ZSESwedenBaseParser {

  public ZSESwedenBParser() {
    super("", "",
        "Presentationsgrupp:CALL R_Adress:ADDR R_Plats:CITY R_Samhälle:CITY2 R_Pos-WSG84:GPS R_RAPS-grupp:CH R_HT-text__1:CALL R_HT-text__2:CALL R_HT-text__3:CALL R_Objekt:PLACE R_Adressbeskrivning:PLACE R_Nyckel:MAP R_Zon:UNIT R_HT-kommentar:INFO/N INFO/N",
        FLDPROG_ANY_ORDER | FLDPROG_DOUBLE_UNDERSCORE);
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
    if (!body.startsWith("R_Pos-WSG84 : ")) {
      body = cleanFixedLabelBreaks(body, 20);
      if (body == null) return false;
    }
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.length() == 0 && data.strGPSLoc.length() == 0) return false;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY2")) return new MyCity2Field();
    return super.getField(name);
  }

  private class MyCity2Field extends CityField {
    @Override
    public void parse(String field, Data data) {

      // There are usually two city fields, take the first one that is not blank
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }
}
