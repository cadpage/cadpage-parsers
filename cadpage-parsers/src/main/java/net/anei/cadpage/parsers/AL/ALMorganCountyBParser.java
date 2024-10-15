package net.anei.cadpage.parsers.AL;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class ALMorganCountyBParser extends FieldProgramParser {

  public ALMorganCountyBParser() {
    super(CITY_LIST, "MORGAN COUNTY", "AL",
         "( SELECT/RR RR_INFO+ " +
         "| CALL! Location:ADDRCITYST! Desc:DESC? Loc_Name:PLACE! TAC:SRC_CH! SRC_CH+? Assigned:DATETIME! Detail:INFO! INFO/N+ " +
         ")");
  }

  @Override
  public String getFilter() {
    return "cad-no-reply@morgan911.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CFS - (Unit Assigned|Changed|Completed) - #(.*)");
  private static final Pattern DELIM_PTN1 = Pattern.compile(";?\n");

  private Set<String> channelSet = new HashSet<>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;

    channelSet.clear();

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    if (match.group(1).equals("Completed")) {
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("RR");
    } else {
      setSelectValue("");
    }
    data.strCallId = match.group(2).trim();
    return parseFields(DELIM_PTN1.split(body), data);
  }

  @Override
  public String getProgram() {
    return "ID CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DESC")) return new MyDescField();
    if (name.equals("SRC_CH")) return new MySourceChannelField();
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d:\\d\\d)\\b[ *]*", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("RR_INFO")) return new MyRRInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern APT_DESC_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM)[. ]*(\\S+) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("\\d+[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyDescField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;

      Matcher match = APT_DESC_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        data.strPlace = match.group(2);
      }
      else if (APT_PTN.matcher(field).matches()) {
        data.strApt = append(data.strApt, "-", field);
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern SRC_CHANNEL_PTN = Pattern.compile("(.*) - (TAC\\d+)");
  private class MySourceChannelField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("None")) return true;
      Matcher match = SRC_CHANNEL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strSource = append(data.strSource, ",", match.group(1).trim());
      String channel = match.group(2);
      if (channelSet.add(channel)) data.strChannel = append(data.strChannel, ",", channel);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "SRC CH";
    }
  }

  private static final Pattern INFO_PFX_PTN = Pattern.compile("[; ]*\\b(?:\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d [-\\*] *)+");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String line : INFO_PFX_PTN.split(field)) {
        line = line.trim();
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("\\bLat (\\S+); Lon (\\S+)\\b");
  private class MyRRInfoField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) {
        String key = field.substring(0, pt).trim();
        String value = field.substring(pt+1).trim();

        switch (key) {
        case "911 Information":
          Matcher match = GPS_PTN.matcher(value);
          if (match.find()) {
            setGPSLoc(match.group(1)+','+match.group(2), data);
          }
          return;

        case "911 Calling Number":
          data.strPhone = value;
          return;

        case "Additional Incident Codes":
          data.strCode = value;
          return;

        case "Additional Incident Codes Description":
          data.strCall = value;
          return;

        case "CFS Location Address":
          super.parse(value, data);
          return;

        case "CFS Location Common":
          data.strPlace = value;
          return;

        case "CFS Number":
          data.strCallId = value;
          return;

        case "911 Class of Service":
        case "911 ESN":
        case "911 Latitude":
        case "911 Longitude":
        case "911 Uncertainty":
        case "CFS Entered Location":
        case "CFS Location Street Only":
        case "CFS Location Details":
        case "CFS Location Latitude":
        case "CFS Location Longitude":
        case "CFS Response Times":
        case "Incident Code":
        case "Incident Code Description":
          return;
        }
      }
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "GPS PHONE CODE CALL ADDR APT CITY ST PLACE ID INFO";
    }

  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "FALKVILLE",
      "BERKELEY SPRINGS",
      "PAW PAW",

      // Magisterial districts
      "ALLEN",
      "BATH",
      "CACAPON",
      "ROCK GAP",
      "SLEEPY CREEK",
      "TIMBER RIDGE",

      // Census-designated places
      "GREAT CACAPON",

      // Unincorporated communities
      "BERRYVILLE",
      "BURNT FACTORY",
      "CAMPBELLS",
      "CHERRY RUN",
      "DOE GULLY",
      "DUCKWALL",
      "GREEN RIDGE",
      "GREENWOOD",
      "HANCOCK",
      "HANSROTE",
      "HOLTON",
      "JEROME",
      "JIMTOWN",
      "JOHNSONS MILL",
      "LARGENT",
      "LINEBURG",
      "MAGNOLIA",
      "MOUNT TRIMBLE",
      "NEW HOPE",
      "NORTH BERKELEY",
      "OAKLAND",
      "OMPS",
      "ORLEANS CROSS ROADS",
      "REDROCK CROSSING",
      "RIDERSVILLE",
      "RIDGE",
      "ROCK GAP",
      "SIR JOHNS RUN",
      "SLEEPY CREEK",
      "SMITH CROSSROADS",
      "SPOHRS CROSSROADS",
      "STOTLERS CROSSROADS",
      "UNGER",
      "WOODMONT",
      "WOODROW"

  };

}
