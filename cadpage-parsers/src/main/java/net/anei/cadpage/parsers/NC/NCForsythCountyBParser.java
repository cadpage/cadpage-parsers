package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCForsythCountyBParser extends FieldProgramParser {

  public NCForsythCountyBParser() {
    super(CITY_CODES, "FORSYTH COUNTY", "NC",
         "( CANCEL ADDR CITY! PLACE " +
         "| CALL PRI ADDR PLACE CITY X X SRC UNIT TYPE PLACE CH ID! " +
         ") END");
  }

  @Override
  public String getFilter() {
    return "CAD@fcso.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    if (!body.startsWith("CAD - ")) return false;
    return parseFields(body.substring(6).trim().split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("(?:\\{\\S+\\} +)?(.*\\bCANCEL\\b.*|RE-DISPATCH)", true);
    if (name.equals("PRI")) return new PriorityField("\\d|P", true);
    if (name.equals("TYPE")) return new SkipField("EMS|FIRE", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ID")) return new IdField("\\d{5,}", true);
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(field, " - ", data.strPlace);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BE",   "BETHANIA",
      "BELE", "BELEWS CREEK",
      "BETH", "BETHANIA",
      "CL",   "CLEMMONS",
      "CLEM", "CLEMMONS",
      "COLF", "COLFAX",
      "DAN",  "DANBURY",
      "DC",   "DAVIDSON COUNTY",
      "EDEN", "EDEN",
      "FC",   "FORSYTH COUNTY",
      "GC",   "GUILFORD COUNTY",
      "GER",  "GERMANTON",
      "GERM", "GERMANTON",
      "HP",   "HIGH POINT",
      "KE",   "KERNERSVILLE",
      "KER",  "KERNERSVILLE",
      "KI",   "KING",
      "KIN",  "KING",
      "KING", "KING",
      "LAW",  "LAWSONVILLE",
      "LEW",  "LEWISVILLE",
      "MAD",  "MADISON",
      "PFAF", "PFAFFTOWN",
      "PIN",  "PINNACLE",
      "RH",   "RURAL HALL",
      "SC",   "STOKES COUNTY",
      "STA",  "STANLEYVILLE",
      "STAN", "STANLEYVILLE",
      "STOK", "STOKESDALE",
      "SU",   "SURRY",
      "TO",   "TOBACCOVILLE",
      "TOB",  "TOBACCOVILLE",
      "TOBA", "TOBACCOVILLE",
      "WA",   "WALKERTOWN",
      "WAL",  "WALNUT COVE",
      "WALK", "WALKERTOWN",
      "WALN", "WALNUT COVE",
      "WC",   "WALNUT COVE",
      "WES",  "WESTFIELD",
      "WS",   "WINSTON-SALEM"

  });

}
