package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOTexasCountyParser extends FieldProgramParser {

  public MOTexasCountyParser() {
    super(CITY_LIST, "TEXAS COUNTY", "MO",
          "Event_Number:ID! Category:CALL! CALL/S+ Sub_Category:CALL/S! CALL/S+ Status_Code:SKIP! Address:ADDRCITY! INCIDENT_NOTES%EMPTY! INFO/N+");
    setupCities(MISSPELLED_CITIES);
  }

  @Override
  public String getFilter() {
    return "texascounty911@omnigo.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITIES);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)  (.*),.*");
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(",")) return;
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = match.group(2).trim();
      } else {
        int pt = field.lastIndexOf(',');
        if (pt >= 0) field = field.substring(0,pt).trim();
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Note Data")) return;
      super.parse(field, data);
    }
  }

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "SUMMERSVILLVE",    "SUMMERSVILLE"
  });

  private static final String[] CITY_LIST = new String[] {

        // Cities
        "CABOOL",
        "HOUSTON",
        "LICKING",
        "MOUNTAIN GROVE",
        "SUMMERSVILLE",

        // Villages
        "PLATO",
        "RAYMONDVILLE",

        // Unincorporated communities
        "ALICE",
        "ARROLL",
        "ASHLEY CREEK",
        "BADO",
        "BENDAVIS",
        "BIG CREEK",
        "BUCYRUS",
        "CLARA",
        "CLEAR SPRINGS",
        "DENT",
        "DUNN",
        "DYKES",
        "ELK CREEK",
        "ELLIS PRAIRIE",
        "ELLSWORTH",
        "EUNICE",
        "EVENING SHADE",
        "FOWLER",
        "GUILD",
        "HARTSHORN",
        "HUGGINS",
        "KIMBLE",
        "KINDERPOST",
        "LADD",
        "LUNDY",
        "MAHAN",
        "MAPLES",
        "MITCHELLS CORNER",
        "NILE",
        "OSCAR",
        "PLEASANT RIDGE",
        "PRESCOTT",
        "ROBY",
        "SAMOA",
        "SARGENT",
        "SHERRILL",
        "SIMMONS",
        "SOLO",
        "STULTZ",
        "SUCCESS",
        "TYRONE",
        "UPTON",
        "VADA",
        "VARVOL",
        "VENABLE",
        "YUKON",

        // Townships
        "BOONE",
        "BURDINE",
        "CARROLL",
        "CASS",
        "CLINTON",
        "CURRENT",
        "DATE",
        "JACKSON",
        "LYNCH",
        "MORRIS",
        "OZARK",
        "PIERCE",
        "PINEY",
        "ROUBIDOUX",
        "SARGENT",
        "SHERRILL",
        "UPTON",

        // Howell County
        "MOUNTAIN VIEW",

        // Laclede County
        "LYNCHBURG",

        // Phelps County
        "EDGAR SPRINGS",

        // Shannon County
        "EMINENCE"

  };
}
