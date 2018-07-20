package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAFluvannaCountyCParser extends FieldProgramParser {
  
  public VAFluvannaCountyCParser() {
    super(CITY_CODES, "FLUVANNA COUNTY", "VA", 
          "CALL1 CALL2/SDS ADDRCITY BOX UNIT! INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new CallField("[A-Z]+", true);
    if (name.equals("CALL2")) return new CallField("[- /A-Z0-9]+", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("BOX")) return new BoxField("\\S+", true);
    if (name.equals("UNIT")) return new UnitField("\\S+", true);
    return super.getField(name);
  }
  
  private class MyAddressCityField extends Field {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      data.strPlace = p.getLastOptional(';');
      String apt = p.getLastOptional('#');
      parseAddress(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BRB", "BREMO BLUFF",
      "COL", "COLUMBIA",
      "FKU", "FORK UNION",
      "KES", "KESWICK",
      "KTS", "KENTS STORE",
      "PAL", "PALMYRA",
      "SCT", "SCOTTSVILLE",
      "TRY", "TROY"
  });

}
