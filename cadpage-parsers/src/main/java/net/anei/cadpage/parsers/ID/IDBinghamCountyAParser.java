package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDBinghamCountyAParser extends FieldProgramParser {

  public IDBinghamCountyAParser() {
    this("BINGHAM COUNTY", "ID");
  }

  public IDBinghamCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "ID:ID! CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY! LAT:GPS1! LONG:GPS2! PRI:PRI? DATE:DATE! TIME:TIME! INFO:INFO INFO/N+ UNIT:UNIT");
  }

  @Override
  public String getFilter() {
    return "rims@co.fremont.id.us,active911@shoshoneso.com,active911@benewahcounty.org,active911@boundarysheriff.org";
  }

  @Override
  public String getAliasCode() {
    return "IDBinghamCounty";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

}
