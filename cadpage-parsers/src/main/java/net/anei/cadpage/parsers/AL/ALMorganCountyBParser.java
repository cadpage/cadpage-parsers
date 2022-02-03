package net.anei.cadpage.parsers.AL;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALMorganCountyBParser extends FieldProgramParser {

  public ALMorganCountyBParser() {
    super(CITY_LIST, "MORGAN COUNTY", "AL",
          "( SELECT/1 CALL! Location:ADDRCITYST! | Location:ADDR/S! ) Desc:DESC? Loc_Name:PLACE! TAC:SRC_CH! SRC_CH+? Assigned:DATETIME! Detail:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cad-no-reply@morgan911.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CFS - Unit Assigned - #(.*)");
  private static final Pattern DELIM_PTN1 = Pattern.compile(";?\n");

  private Set<String> channelSet = new HashSet<>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;

    channelSet.clear();

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strCallId = match.group(1).trim();
      setSelectValue("1");
      return parseFields(DELIM_PTN1.split(body), data);
    }
    int pt = subject.indexOf(';');
    if (pt >= 0) subject = subject.substring(0,pt).trim();
    subject = stripFieldEnd(subject, " None");
    data.strCall = subject;

    if (body.startsWith("Location:")) {
      body = stripFieldEnd(body, ";");
      setSelectValue("2");
      body = body.replace("Tac:", "TAC:").replace("detail:", "Detail:");
      return parseFields(body.split("; "), data);
    }

    return false;
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
