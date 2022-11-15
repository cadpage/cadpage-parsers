package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIMontcalmCountyParser extends DispatchOSSIParser {

  public MIMontcalmCountyParser() {
    super(CITY_CODES, "MONTCALM COUNTY", "MI",
          "( CANCEL ADDR CITY/Y " +
          "| FYI? ( ( ID2 UNIT? | UNIT ) CALL ADDR! DUPADDR? ( CITY/Y! | SKIP CITY/Y! | SKIP EMPTY/Z CITY/Y! | CITY? ) X+? ( SKIP ID | ID? ) " +
                 "| ( DATETIME ADDR | ADDR ID? DATETIME? ) CITY? ( ID | CALL! X+? ) " +
                 ") " +
          ") INFO/N+");
    addRoadSuffixTerms("LP");
  }

  @Override
  public String getFilter() {
    return "cad@mydomain.com";
  }

  private String saveAddress;

  @Override
  protected boolean parseMsg(String body, Data data) {
    saveAddress = null;
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "EMS ALERT";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:\\b[A-Z]+\\d+|Temp\\d+|ALLRESCUES|AERO|BELDM|GRATM|MECOM|MTRT|NEWYM|OTHRM|ROCKM|SLR)\\b,?)+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DUPADDR")) return new MyDupAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("\\d{7}", true);
    if (name.equals("ID2"))  return new IdField("\\d+", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      saveAddress = field;
      super.parse(field, data);
    }
  }

  private class MyDupAddressField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(saveAddress);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      String oldCity = data.strCity;
      if (!super.checkParse(field, data)) return false;
      if (oldCity.length() > 0) {
        if (!oldCity.endsWith(" TWP") || data.strCity.endsWith(" TWP")) {
          data.strCity = oldCity;
        }
      }
      return true;
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (checkAddress(field) != STATUS_STREET_NAME) return false;
      super.parse(field, data);
      return true;
    }

  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Event spawned from ");
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-Comments:");
      if (field.equals(saveAddress)) return;
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BELV", "BELVIDERE TWP",
      "BLOO", "BLOOMER TWP",
      "BUSH", "BUSHNELL TWP",
      "CARS", "CARSON CITY",
      "CATO", "CATO TWP",
      "CDLK", "CEDAR LAKE",
      "CORA", "CORAL",
      "CRYS", "CRYSTAL TWP",
      "DAY",  "DAY TWP",
      "DOUG", "DOUGLASS TWP",
      "EDMO", "EDMORE",
      "EURE", "EUREKA TWP",
      "EVER", "EVERGREEN TWP",
      "FAIR", "FAIRPLAIN TWP",
      "FENW", "FENWICK",
      "FERR", "FERRIS TWP",
      "GOWE", "GOWEN",
      "GREE", "GREENVILLE",
      "HOME", "HOME TWP",
      "HOWA", "HOWARD CITY",
      "LAKE", "LAKEVIEW",
      "MAPL", "MAPLE VALLEY TWP",
      "MCBR", "MCBRIDE",
      "MONT", "MONTCALM TWP",
      "NEWA", "NEWARK",
      "PIER", "PIERSON TWP",
      "PINE", "PINE TWP",
      "REYN", "REYNOLDS TWP",
      "RICH", "RICHLAND TWP",
      "SAND", "SAND LAKE",
      "SHER", "SHERIDAN",
      "SIDN", "SIDNEY TWP",
      "SIXL", "SIX LAKES",
      "STAN", "STANTON",
      "VEST", "VESTABURG",
      "WINF", "WINFIELD TWP",

  });
}
