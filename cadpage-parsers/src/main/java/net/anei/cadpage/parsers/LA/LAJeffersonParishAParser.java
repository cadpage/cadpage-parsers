package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Jefferson Parish, LA
 */
public class LAJeffersonParishAParser extends FieldProgramParser {

  private static final Pattern MARKER = Pattern.compile("^DISPATCH From [A-Z]{2,3}\\d+:");
  private static final Pattern CITY_CROSS_PTN = Pattern.compile("\\b([A-Z]{2}) +(<\\d+/ *\\d+>)");

  public LAJeffersonParishAParser() {
    super(CITY_CODES, "JEFFERSON PARISH", "LA",
        "CODE CALL ADDR CITY! ( AT CITY | ) X? SRC MAP MAPPAGE? XXXX? INFO+ Units:UNIT UNIT+");
  }

  @Override
  public String getFilter() {
    return "@dispatchtext.com,CAD@JEFFPARISH.NET,911relay@jeffparish.gov,Cad@jpcd.net,cad@marreroestellefire.com,5043829663@vzwpix.com,donotreply@kennerpd.com";
  }

  @Override
  public String adjustMapAddress(String addr) {
    return PK_PTN.matcher(addr).replaceAll("PKWY");
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end()).trim();
    body = CITY_CROSS_PTN.matcher(body).replaceAll("$1, $2");
    body = body.replace(" Unit:", " Units:");
    if (!parseFields(body.split(","), data)) return false;
    if (data.strUnit.length() == 0) data.strUnit = data.strSource;
    return true;
  }

  private class MyAddressField extends AddressField {
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('<');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  // The AT field starts with an at keyword
  // It indicates that this is the real address, and what we originally
  // parsed as an address is a place name
  private class AtField extends AddressField {

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("at ")) return false;
      data.strPlace = data.strAddress;
      data.strAddress = "";
      parse(field.substring(3).trim(), data);
      if (data.strAddress.equals(data.strPlace)) data.strPlace = "";
      return true;
    }
  }

  // Cross field only exist if it has the correct keyword
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("btwn ")) {
        field = field.substring(5).trim();
      } else if (field.startsWith("<") && field.endsWith(">")) {
        if (field.equals("<0/0>")) field = "";
      } else return false;
      super.parse(field, data);
      return true;
    }
  }

  // Unit fields join together with comma separators
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" -");
      if (pt >= 0) field = field.substring(0,pt).trim();
      data.strUnit = append(data.strUnit, ",", field);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("[A-Z0-9]{1,6}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("AT")) return new AtField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{3,4}", true);
    if (name.equals("MAP")) return new MapField("[A-Z0-9]{3,6}", true);
    if (name.equals("MAPPAGE")) return new SkipField("mappage", true);
    if (name.equals("XXXX")) return new SkipField("XXXX?", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AV", "Avondale",
      "BA", "Barataria",
      "BC", "Bridge City",
      "CP", "Crown Point",
      "JE", "Jefferson",
      "GI", "Grand Isle",
      "GU", "Terrytown",
      "GR", "Gretna",
      "HA", "Harahan",
      "HY", "Harvey",
      "KE", "Kenner",
      "LB", "Lafitte",
      "LU", "Lafitte",
      "MA", "Marrero",
      "ME", "Metairie",
      "RR", "River Ridge",
      "TT", "Terrytown",
      "WA", "Waggaman",
      "WU", "Westwego",
      "WE", "Westwego",
  });
}
