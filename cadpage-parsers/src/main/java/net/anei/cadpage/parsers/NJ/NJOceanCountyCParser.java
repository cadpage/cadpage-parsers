package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJOceanCountyCParser extends FieldProgramParser {

  public NJOceanCountyCParser() {
    super("OCEAN COUNTY", "NJ",
          "SRC CALL ADDR UNIT MAP INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Hiplink@co.ocean.nj.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Email Copy Message From Hiplink") &&
        !subject.equals("Message from HipLink")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if  (name.equals("UNIT")) return new UnitField("[-A-Z0-9]+", true);
    if (name.equals("MAP")) return new MapField("[A-Z0-9]+", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      data.strPlace = p.getLastOptional(';');
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BER", "BERKELEY TWP",
      "BEW", "PINE BEACH", //???
      "BGT", "BARNEGAT",
      "LAC", "LACEY TWP",
      "LKH", "LAKEHURST",
      "MNT", "MANTOLOKING",
      "OCG", "",            //???
      "OCT", "OCEAN TWP",
      "OGT", "OCEAN GATE",
      "PLU", "PLUMSTED TWP",
      "PNB", "PINE BEACH",
      "TRV", "TOMS RIVER"
  });
}
