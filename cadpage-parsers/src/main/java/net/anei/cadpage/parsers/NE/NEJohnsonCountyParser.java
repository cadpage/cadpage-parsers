package net.anei.cadpage.parsers.NE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NEJohnsonCountyParser extends FieldProgramParser {

  public NEJohnsonCountyParser() {
    super("JOHNSON COUNTY", "NE",
          "Incident_code:CALL! Address:ADDR! CITY! ST_ZIP? Address_details:PLACE! GPS? INFO/CS+");
  }

  @Override
  public String getFilter() {
    return "scans@johnsoncounty.ne.gov,centralsquare@johnsoncountyne.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(","), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST_ZIP")) return new StateField("([A-Z]{2})(?: +\\d{5})?", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|LOT) +(.*)|(\\d{1,4}[A-Z]?|[A-Z])");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
        if (data.strApt == null) data.strApt = match.group(2);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }

  private class MyGPSField extends GPSField {
    public MyGPSField() {
      super("\\d+\\.\\d+-\\d+\\.\\d+|NoneNone", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
