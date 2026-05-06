package net.anei.cadpage.parsers.MN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MNWinonaCountyParser extends FieldProgramParser {

  public MNWinonaCountyParser() {
    super("WINONA COUNTY", "MN",
          "INCIDENT_TYPE:CALL! CALL_TIME:DATETIME! COMMONPLACE_NAME:PLACE! INCIDENT_ADDRESS:ADDRCITYST! LOCATION_DETAILS:LOC_DETAILS! CALL_DETAILS:INFO! EMPTY! END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "noreply@co.winona.mn.us";
  }

  private Pattern DELIM = Pattern.compile(" \\| ");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body+' ', -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("LOC_DETAILS")) return new MyLocationDetailsField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(.*?) *\\b(?:APT|RM|ROOM|LOT) +(.*)|(\\d{1,4}[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);
  private class MyLocationDetailsField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        String apt = match.group(2);
        if (apt == null) apt = match.group(3);
        data.strApt = append(data.strApt, "-", apt);
      }
      data.strPlace = append(data.strPlace, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }

  private Pattern INFO_HDR_PTN = Pattern.compile("^\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_HDR_PTN.matcher(field).replaceFirst("");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "21121 HWY 61",                         "+44.024670,-91.589470",
      "21530 HWY 61",                         "+44.024980,-91.569740",
      "21884 HWY 61",                         "+44.020540,-91.551310",
      "22194 HWY 61",                         "+44.019500,-91.531390",
      "22486 HWY 61",                         "+44.014090,-91.513110",
      "22750 HWY 61",                         "+44.006910,-91.496290",
      "23030 HWY 61",                         "+44.004870,-91.476850",
      "23562 HWY 61",                         "+43.996680,-91.461310",
      "24168 HWY 61",                         "+43.989820,-91.443470",
      "24760 HWY 61",                         "+43.980570,-91.430420",
      "25482 HWY 61",                         "+43.968390,-91.420270"
  });
}
