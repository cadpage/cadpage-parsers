package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCRandolphCountyParser extends FieldProgramParser {

  public NCRandolphCountyParser() {
    super("RANDOLPH COUNTY", "NC",
          "SRC ( UNIT ID! INFO/R INFO/N+ | CALL ADDRCITY UNIT SKIP! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "911@RandolphCountyNC.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Randolph 911 - ");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]+[0-9]+|C?\\d+-\\d+|[A-Z]+[FP]D|ASH[A-Z]{1,2}|\\d{3}|", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "\"");
      field = stripFieldEnd(field, "\"");
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      data.strCity = convertCodes(city, CITY_CODES);
      String apt = p.getLastOptional(';');
      if (apt.length() == 0) apt = p.getLastOptional(',');
      parseAddress(p.get(), data);
      if (apt.length() > 0) {
        if (apt.startsWith("MM ")) {
          data.strAddress = append(data.strAddress, ", ", apt);
        } else {
          data.strApt = append(data.strApt, "-", apt);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private static final Pattern SKIP_INFO_PTN = Pattern.compile("^\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() >= 19) {
        if (SKIP_INFO_PTN.matcher(field).find()) return;
      } else {
        if ("NN:NN:NN NN/NN/NNNN".startsWith(field.replaceAll("\\d", "N"))) return;
      }
      if (field.length() >= 5) {
        if (field.toUpperCase().startsWith("PROQA")) return;
      } else {
        if ("PROQA".startsWith(field.toUpperCase())) return;
      }
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return CTRY_PTN.matcher(addr).replaceAll("COUNTRY");
  }
  private static final Pattern CTRY_PTN = Pattern.compile("CTRY\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARC", "ARCHDALE",
      "ASB", "ASHEBORO",
      "ASH", "ASHEBORO",
      "BEN", "BENNETT",
      "CLI", "CLIMAX",
      "DEN", "DENTON",
      "FRA", "FRANKLINVILLE",
      "GRE", "GREENSBORO",
      "HIG", "HIGH POINT",
      "JUL", "JULIAN",
      "LEX", "LEXINGTON",
      "LIB", "LIBERTY",
      "PLE", "PLEASANT GARDEN",
      "RAM", "RAMSEUR",
      "RAN", "RANDLEMAN",
      "ROB", "ROBBINS",
      "SEA", "SEAGROVE",
      "SIL", "SILER CITY",
      "SOP", "SOPHIA",
      "STA", "STALEY",
      "THO", "THOMASVILLE",
      "TRO", "TROY",
      "TRI", "TRINITY"

  });
}
