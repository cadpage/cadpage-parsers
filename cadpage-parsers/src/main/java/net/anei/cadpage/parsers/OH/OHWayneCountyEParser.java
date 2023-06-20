package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class OHWayneCountyEParser extends FieldProgramParser {

  public OHWayneCountyEParser() {
    super("WAYNE COUNTY", "OH",
          "ADDR CALL INFO! X:X! ID! DATETIME! END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "noreply@zuercherportal.com";
  }

  private static final Pattern DELIM = Pattern.compile(" /(?= )");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("/")) body = " / " +  body;
    return parseFields(DELIM.split(body), data);
  }

  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("[A-Z]{1,3}\\d+", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');

      String zip = null;
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        zip =  match.group(2);
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip !=  null) city = convertCodes(zip, ZIP_CODE_TABLE);
      if (city.equals("SAL")) city = "SALT CREEK TWP";
      data.strCity = city;

      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile(";? *\\b(?:(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) - +)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String part : INFO_DATE_TIME_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", part.trim());
      }
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Properties ZIP_CODE_TABLE = buildCodeTable(new String[]{
      "44230", "DOYLESTOWN",
      "44270", "RITTMAN",
      "44606", "APPLE CREEK",
      "44613", "BREWSTER",
      "44614", "CANAL FULTON",
      "44618", "DALTON",
      "44624", "DUNDEE",
      "44627", "FREDERICKSBURG",
      "44636", "KIDRON",
      "44645", "MARSHALLVILLE",
      "44646", "MASSILLON",
      "44647", "MASSILLON",
      "44659", "MT EATON",
      "44611", "BIG PRAIRIE",
      "44662", "NAVARRE",
      "44666", "NORTH LAWRENCE",
      "44667", "ORRVILLE",
      "44676", "SHREVE",
      "44677", "SMITHVILLE",
      "44689", "WILMOT"
  });

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "580 E SUMMIT ST",                      "+40.977530,-81.694290",
      "48 EDWARDS DR",                        "+40.977530,-81.694290"
  });
}
