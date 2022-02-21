package net.anei.cadpage.parsers.OK;

import java.util.regex.Pattern;


import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA30Parser;


public class OKCarterCountyBParser extends DispatchA30Parser {


  public OKCarterCountyBParser() {
    super(OKCarterCountyParser.CITY_LIST, "E", "CARTER COUNTY", "OK");
    addExtendedDirections();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("RURAL ADDRESS / ")) {
        data.strSupp = "RURAL ADDRESS";
        field = field.substring(16).trim();
      }
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return PK_PTN.matcher(sAddress).replaceAll("PKWY");
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
}
