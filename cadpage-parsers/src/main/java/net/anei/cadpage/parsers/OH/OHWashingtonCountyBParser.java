package net.anei.cadpage.parsers.OH;

/**
 * Washington County, OH (B)
 */

import net.anei.cadpage.parsers.FieldProgramParser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWashingtonCountyBParser extends FieldProgramParser {

  public OHWashingtonCountyBParser () {
    super(CITY_LIST, "WASHINGTON COUNTY", "OH",
          "( Call_Date:DATE! Call_Time:TIME! Fire_Code:CALL! Location:ADDR! Owner:PLACE! Sector:MAP! Description:INFO! " +
            "Cross_Street1:X! Cross_Street2:X! Alert1:ALERT! Alert2:ALERT/SDS! " +
          "| DATETIME2 CALL2 ( ADDR2 INFO2! INFO2/N X2 " +
                            "| ADDR/S CITY3! INFO! INFO/N+ CROSS_STREETS:X3! ) " +
          ") END");
    setupCities(WV_CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "belprepd@gmail.com,admin@belprepd.com,cadbelpre@idatp.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page (\\d\\d-\\d{6})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strCallId = match.group(1);
    if (!parseFields(body.split("\n"), 4, data)) return false;
    if (WV_CITY_LIST.contains(data.strCity.toUpperCase())) data.strState = "WV";
    return true;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("DATETIME2")) return new MyDateTimeField();
    if (name.equals("CALL2")) return new MyCallField();
    if (name.equals("ADDR2")) return new MyAddressField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("CITY3")) return new MyCity3Field();
    if (name.equals("X3")) return new MyCross3Field();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) @ (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Report of -- ");
      field = stripFieldEnd(field, " at");
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*)\\(Sector=(\\d+)\\)");
  private static final Pattern ADDRESS_PTN1 = Pattern.compile("At (.*?),(.*?)\\((.*)\\)");
  private static final Pattern ADDRESS_PTN2 = Pattern.compile("(.*?) / *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strMap = match.group(2);
      }
      match = ADDRESS_PTN1.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim().replace('@', '&'), data);
        data.strCity = match.group(2).trim();
        data.strPlace = match.group(3).trim();
        return true;
      }

      match = ADDRESS_PTN2.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim().replace('@', '&'), data);
        data.strPlace = match.group(2).trim();
        return true;
      }

      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field,  data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE MAP";
    }
  }

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Call Description is=> ");
      super.parse(field, data);
    }
  }

  private static final Pattern CROSS_PTN = Pattern.compile("Cross Streets are - (.*?) and\\b(.*)");
  private class MyCross2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CROSS_PTN.matcher(field);
      if (match.matches()) {
        data.strCross = append(match.group(1).trim(), " / ", match.group(2).trim());
      } else {
        if (!field.startsWith("Cross Streets")) abort();
        String cross = field.substring(13).trim();
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
        data.strCross = cross;
      }
    }
  }

  private class MyCity3Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }

  private class MyCross3Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("/")) {
        field = stripFieldStart(field, "/");
        field = stripFieldEnd(field, "/");
        super.parse(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, field, data);
      }
    }
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "BELPRE",
      "MARIETTA",

      // Villages
      "BEVERLY",
      "LOWER SALEM",
      "LOWELL",
      "MACKSBURG",
      "MATAMORAS",

      // Townships
      "ADAMS",
      "AURELIUS",
      "BARLOW",
      "BELPRE",
      "DECATUR",
      "DUNHAM",
      "FAIRFIELD",
      "FEARING",
      "GRANDVIEW",
      "INDEPENDENCE",
      "LAWRENCE",
      "LIBERTY",
      "LUDLOW",
      "MARIETTA",
      "MUSKINGUM",
      "NEWPORT",
      "PALMER",
      "SALEM",
      "WARREN",
      "WATERFORD",
      "WATERTOWN",
      "WESLEY",

      // Census-designated places
      "DEVOLA",
      "LITTLE HOCKING",
      "NEWPORT",
      "RENO",
      "VINCENT",
      "WATERFORD",

      // Unincorporated communities
      "ARCHERS FORK",
      "BARLOW",
      "BARTLETT",
      "BEAVERTOWN",
      "BECKETT",
      "BEVAN",
      "BLOOMFIELD",
      "BONN",
      "BRIGGS",
      "CAYWOOD",
      "CHURCHTOWN",
      "COAL RUN",
      "CONSTITUTION",
      "CORNERVILLE",
      "COW RUN",
      "CUTLER",
      "DALZELL",
      "DART",
      "DECATURVILLE",
      "DEUCHER",
      "DUNBAR",
      "DUNHAM",
      "ELBA",
      "EQUITY",
      "FILLMORE",
      "FLEMING",
      "GERMANTOWN",
      "GRACEY",
      "GRANDVIEW",
      "LAYMAN",
      "LEITH",
      "LOWER NEWPORT",
      "LUKE CHUTE",
      "MOORE JUNCTION",
      "MOSS RUN",
      "NEWELL RUN",
      "OAK GROVE",
      "PATTEN MILLS",
      "PINEHURST",
      "QUALEY",
      "RAINBOW",
      "RELIEF",
      "SITKA",
      "STANLEYVILLE",
      "TICK RIDGE",
      "VETO",
      "WADE",
      "WARNER",
      "WATERTOWN",
      "WHIPPLE",
      "WINGETT RUN",
      "YANKEEBURG"
  };

  private static Set<String> WV_CITY_LIST = new HashSet<String>(Arrays.asList(
      // Woods County
      "BLENNERHASSETT",
      "LITTLE HOCKING",
      "LUBECK",
      "NORTH HILLS",
      "PARKERSBURG",
      "VIENNA",
      "WASHINGTON"
  ));
}