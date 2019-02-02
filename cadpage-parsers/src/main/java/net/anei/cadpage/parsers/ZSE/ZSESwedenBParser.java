package net.anei.cadpage.parsers.ZSE;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenBParser extends ZSESwedenBaseParser {
  
  public ZSESwedenBParser() {
    super("", "",
        "Presentationsgrupp:CALL! R_Adress:ADDR R_Plats:CITY R_Samhälle:CITY2 R_Pos-WSG84:GPS R_RAPS-grupp:CH R_HT-text__1:CALL R_HT-text__2:CALL R_HT-text__3:CALL R_Objekt:PLACE R_Adressbeskrivning:PLACE R_Nyckel:MAP R_Zon:UNIT R_HT-kommentar:INFO/N INFO/N", 
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

  // Just to make life complicated, extra line breaks get inserted and seemingly random intervals.  
  // Which messes with the normal line break delimited fields.  We can identify legitimate line breaks
  // because they are followed by a 20 character keyword followed by a colon.  Or by an illegitimate
  // line break less that 20 characters ahead.  Anything else will be deleted
  private static final Pattern BAD_BRK_PTN = Pattern.compile("\n(?![^:\n]{20}:|[^:\n]{0,20}\n)");
  
  protected boolean parseMsg(String body, Data data) {
    body = BAD_BRK_PTN.matcher(body).replaceAll("");
    return parseFields(body.split("\n"), data);
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
