package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * fAlamance county, NC
 */
public class NCPasquotankCountyParser extends DispatchOSSIParser {

  public NCPasquotankCountyParser() {
    super(CITY_CODES, "PASQUOTANK COUNTY", "NC",
          "( CANCEL ADDR! CITY? INFO/N+ " +
          "| FYI? ( ADDR CALL! | CALL ADDR! ) INFO/N+ )");
    addExtendedDirections();
  }

  @Override
  public String getFilter() {
    return "CAD@co.pasquotank.nc.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (body.startsWith("/ Text Message / ")) {
      int pt = body.indexOf(':');
      body = "CAD:" + body.substring(pt+1).trim();
    }

    if (subject.contains(";")) {
      int pt = body.indexOf(":CAD:");
      if (pt < 0) return false;
      pt += 5;
      body = body.substring(0,pt) + subject + ' ' +  body.substring(pt);
    }
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // Treat first field as address unless next field is a better address
      if (checkAddress(getRelativeField(+1)) > checkAddress(field)) return false;
      parse(field, data);
      return true;
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ELIZ", "ELIZABETH CITY"
  });
}