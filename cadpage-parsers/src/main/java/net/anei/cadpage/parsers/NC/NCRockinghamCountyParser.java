package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Buncombe county, NC
 */
public class NCRockinghamCountyParser extends DispatchOSSIParser {

  public NCRockinghamCountyParser() {
    super(CITY_CODES, "ROCKINGHAM COUNTY", "NC",
          "ID?: ( SELECT/1 Incident#:ID1! Report#:EMPTY! Date:DATE! Time_Out:TIME! Nature:CALL! MP:CODE! Business:PLACE! Address:ADDR! City:CITY! Addt_Address:ADDADDR! Cross:X! X+ Subdivision:SKIP! Neighborhood:SKIP Notes:INFO/N! INFO/N+ Units:UNIT! INFO/N+ " +
               "| FYI? ( ADDR/Z CITY X/Z+? CALL2 UNIT2 | CALL2 ADDR APT? X/Z+? ( CITY ID? APT2? ( CODE2 UNIT2? CH? | UNIT2 CH? | CH | PLACE CODE2 UNIT2? CH? | PLACE UNIT2 CH? | PLACE CH | ) | ID CODE2? UNIT2? CH? | CODE2 UNIT2? CH? | UNIT2 CH? | CH ) ) INFO+ )");
  }

  @Override
  public String getFilter() {
    return "@co.rockingham.nc.us,14101,messaging@iamresponding.com,CAD@rockinghamcountync.gov";
  }

  private static final Pattern SPEC_MARKER = Pattern.compile("Communications\n\nDispatch\n\n");
  private static final Pattern DELIM_PTN = Pattern.compile("\n| (?=Report#:|Time Out:|MP:|City:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = SPEC_MARKER.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      body = body.substring(match.end()).trim();
      return parseFields(DELIM_PTN.split(body), data);
    }
    setSelectValue("2");
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();

    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID1")) return new IdField("\\d\\d-\\d{4}-\\d+", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ADDADDR")) return new MyAdditionalAddressField();

    if (name.equals("CALL2")) return new MyCallField();
    if (name.equals("APT")) return new AptField("[A-Z]|\\d{1,3}", true);
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("APT2")) return new AptField("(?:APT|RM|LOT) *(.*)", true);
    if (name.equals("CODE2")) return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Za-z]?", true);
    if (name.equals("UNIT2")) return new UnitField("(?!TAC)[A-Z]{0,3}\\d{1,3}[A-Z]?", true);
    if (name.equals("CH")) return new ChannelField("(?:Radio Channel: *)?(TAC.*)", true);
    return super.getField(name);
  }

  private class MyAdditionalAddressField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse("Addt Address: " + field, data);
    }

    @Override
    public String getFieldNames() {
      return "";
    }
  }

  private class MyCallField extends  CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("{")) {
        int pt = field.indexOf('}');
        if (pt >= 0) {
          data.strUnit = field.substring(1,pt).trim();
          field = field.substring(pt+1).trim();
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "EDEN", "EDEN",
      "MAD",  "MADISON",
      "MAY",  "MAYODAN",
      "PELH", "PELHAM",
      "REID", "REIDSVILLE",
      "RUFF", "RUFFIN",
      "STON", "STONEVILLE",
      "STOK", "STOKESDALE",
      "SUMM", "SUMMERFIELD"
  });
}
