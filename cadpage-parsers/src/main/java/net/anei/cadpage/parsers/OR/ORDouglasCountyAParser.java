package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ORDouglasCountyAParser extends FieldProgramParser {

  public ORDouglasCountyAParser() {
    super(CITY_CODES,"DOUGLAS COUNTY", "OR",
          "DATETIME CALL ADDR CITY_APT PLACE! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatch@co.douglas.or.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, "...");
    body = stripFieldEnd(body, "\n-");
    int pt = body.indexOf("\n--");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!parseFields(body.split("\n"), data)) return false;
    data.strAddress = data.strAddress.replace("138W", "138 W");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("CITY_APT")) return new MyCityAptField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d)(\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2) + ':' + match.group(3);
    }
  }

  private static final Pattern CITY_APT_PTN = Pattern.compile("(?:([A-Z ]+?) +)?#(.*)");
  private class MyCityAptField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CITY_APT_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(getOptGroup(match.group(1)), data);
      data.strApt = append(data.strApt, "-", match.group(2).trim());
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY APT";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AZ", "AZALEA",
      "CC", "COTTAGE GROVE",
      "CG", "COTTAGE GROVE",
      "CN", "CANYONVILLE",
      "CT", "CHEMULT",
      "CU", "CURTIN",
      "CV", "CAMAS VALLEY",
      "CY", "CANYONVILLE",
      "DC", "DAYS CREEK",
      "DI", "DILLARD",
      "DL", "DILLARD",
      "DR", "DRAIN",
      "EL", "ELKTON",
      "GD", "GLENDALE",
      "GL", "GLIDE",
      "GR", "GARDINER",
      "IP", "IDLEYLD PARK",
      "MC", "MYRTLE CREEK",
      "NB", "NORTH BEND",
      "OK", "OAKLAND",
      "RB", "ROSEBURG",
      "RD", "REEDSPORT",
      "RI", "RIDDLE",
      "RP", "REEDSPORT",
      "SC", "SCOTTSBURG",
      "SU", "SUTHERLIN",
      "TI", "TILLER",
      "TM", "TENMILE",
      "TN", "TEN MILE",
      "UM", "UMPQUA",
      "WB", "WINCHESTER BAY",
      "WE", "WESTLAKE",
      "WI", "WILBUR",
      "WN", "WINCHESTER",
      "WS", "WINSTON",
      "YC", "YONCALLA",
      "YN", "YONCALLA"
  });
}
