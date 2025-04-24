package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXHarrisCountyGParser extends FieldProgramParser {

  public TXHarrisCountyGParser() {
    this("HARRIS COUNTY", "TX");
  }

  public TXHarrisCountyGParser(String defCity, String defState) {
    super(TXHarrisCountyParser.CITY_LIST, defCity, defState,
          "CAD#:ID! Call:CALL! UNIT:UNIT? ADDR:ADDRCITYST/S! INFO/N+");
    setupCities(MISSPELLED_CITIES);
  }

  @Override
  public String getAliasCode() {
    return "TXHarrisCountyG";
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,@dispatches.iamresponding.com";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +NOTES::?(?:[A-Z]+- *\\d+: *)?");
  private static final Pattern DELIM = Pattern.compile("[;\n]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] parts = INFO_BRK_PTN.split(body);
    if (!parseFields(DELIM.split(parts[0].replace("-LA PORTE", " LA PORTE")), data)) return false;
    for (int jj = 1; jj < parts.length; jj++) {
      String fld = parts[jj];
//      int pt =  fld.lastIndexOf(':');
//      if (pt >= 0) fld = fld.substring(pt+1).trim();
      data.strSupp = append(data.strSupp, "\n", fld);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', ',').replace("UNIT:", "");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("NOTES::[A-Z]+-\\d+: *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "BAYCLIFF",   "BACLIFF"
  });
}
