package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * La Porte, TX
 */

public class TXLaPorteParser extends DispatchOSSIParser {

  private static final Pattern PREFIX = Pattern.compile("^\\d*:");
  private static final Pattern LINE_BRK_PTN = Pattern.compile(" *\n *");

  public TXLaPorteParser() {
    this("HARRIS COUNTY", "TX");
  }

  protected TXLaPorteParser(String defCity, String defState) {
    super(TXGalvestonCountyAParser.CITY_CODES, defCity, defState,
          "( CANCEL ADDR! CITY? " +
          "| FYI? ID? SRC? ( CALL_ADDR CITY | CALL! ( ADDR/Z CITY! | ADDR/Z UNIT UNIT+? CITY? | PLACE ADDR/Z CITY! | PLACE ADDR/Z UNIT UNIT+? OPT_CITY? | ADDR! ) ) UNIT+? ( ID PRI? | MAP? ) INFO+? DATETIME UNIT? INFO+ " +
          ") INFO+");
  }

  @Override
  public String getAliasCode() {
    return "TXLaPorte";
  }

  @Override
  public String getFilter() {
    return "cad@ossicadpaging";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    // Kema alerts probably do not belong in this parser, but since Active911
    // handles them in the same parser, we will do likewise.
    if (body.startsWith("CANCEL ")) {
      data.strCall = "CANCEL";
      body = body.substring(7).trim();
      int pt = body.indexOf(';');
      if (pt >= 0) {
        data.strSupp = body.substring(pt+1).trim();
        body = body.substring(0,pt).trim();
      }
      parseAddress(body, data);
      return true;
    }

    // Regular parsing takes up here
    else {

      // Strip off option number prefix
      Matcher match = PREFIX.matcher(body);
      if (match.find()) body = body.substring(match.end()).trim();
      body = LINE_BRK_PTN.matcher(body).replaceAll(" ");
    }

    if (body.startsWith("CAD ") || body.startsWith("CAD\n")) body = "CAD:" + body.substring(4);
    else if (!body.startsWith("CAD:")) body = "CAD:" + body;

    // Both cases invoke the superclass parseMsg method
    return super.parseMsg(body, data);
  }

  private static final Pattern LEAD_TRASH_PTN = Pattern.compile("^(?:Event spawned from |\\(S\\) *\\(N\\)) *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (isValidCrossStreet(field)) {
        data.strCross = append(data.strCross, " / ", field);
      } else {

        Matcher match = LEAD_TRASH_PTN.matcher(field);
        if (match.find()) field = field.substring(match.end());
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new CallField("CANCEL", true);
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("PRI")) return new PriorityField("\\d");
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("OPT_CITY")) return new MyOptCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CODE")) return new CodeField("[A-Z]{1,2}[A-Z0-9]{1,2}", true);
    if (name.equals("MAP")) return new MapField("\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MySourceField extends SourceField {
    public MySourceField() {
      super("(?!ASFD)[A-Z]{4}", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strSource = append(data.strSource, " ", field);
    }
  }

  private class MyCallAddressField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_WATERFRONT_PTN.matcher(field);
      if (match.matches()) {
        data.strAddress = match.group(1) + " KEMAH WATER FRONT";
        return;
      }
      Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, field);
      if (res.getStatus() > STATUS_NOTHING) {
        res.getData(data);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT";
    }
  }

  private static final Pattern ADDR_WATERFRONT_PTN = Pattern.compile("(\\d+) KEMAH WATERFRONT");
  private class MyAddressField extends AddressField {
    @Override public void parse(String field, Data data) {
      Matcher match = ADDR_WATERFRONT_PTN.matcher(field);
      if (match.matches()) {
        data.strAddress = match.group(1) + " KEMAH WATER FRONT";
        return;
      }

      if (data.strCity.length() == 0) {
        int pt = field.lastIndexOf(',');
        if (pt >= 0) {
          data.strCity = convertCodes(field.substring(pt+1).trim().toUpperCase(), TXGalvestonCountyAParser.CITY_CODES);
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }
  }

  private static final String UNIT_PTN1 = "(?:[A-Z]+\\d+|[A-Z]{2,4}|\\d{2,4})";
  private static final String UNIT_PTN = UNIT_PTN1 + "(?:," + UNIT_PTN1 + ")*";
  private class MyUnitField extends UnitField {

    public MyUnitField() {
      super(UNIT_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, ",", field);
    }
  }

  private class MyOptCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCity.length() > 0) return false;
      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  @Override
  public String adjustMapAddress(String address) {
    address = PVT_DR_PTN.matcher(address).replaceAll("");
    address = DASH_ALPH_PTN.matcher(address).replaceAll("");
    address = HALF_PTN.matcher(address).replaceAll("1/2");
    address = IH45_PTN.matcher(address).replaceAll("GULF FWY");
    return address.trim();
  }
  private static final Pattern PVT_DR_PTN = Pattern.compile("\\(PVT.*?\\)(?: *(?:DR|RD)\\b)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern DASH_ALPH_PTN = Pattern.compile("-(?:SH|ST|LA).*$");
  private static final Pattern HALF_PTN = Pattern.compile("\\bHALF\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern IH45_PTN = Pattern.compile("\\bIH *45(?: *FWY)?", Pattern.CASE_INSENSITIVE);
}
