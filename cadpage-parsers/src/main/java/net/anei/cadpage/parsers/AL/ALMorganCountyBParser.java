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
          "Location:ADDR/S! Loc_Name:PLACE! Tac:SRC_CH! SRC_CH+? Assigned:UNIT! detail:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cad-no-reply@morgan911.org";
  }

  private static final Pattern MASTER = Pattern.compile("([^;\n]+?) (?:([A-Z][a-z][ A-Za-z]*(?: V?FD)?|MCSO) - (TAC\\d+)|None)\\b(.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^|; *)\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");

  private Set<String> channelSet = new HashSet<>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    int pt = subject.indexOf(';');
    if (pt >= 0) subject = subject.substring(0,pt).trim();
    subject = stripFieldEnd(subject, " None");
    data.strCall = subject;

    if (body.startsWith("Location:")) {
      body = stripFieldEnd(body, ";");
      channelSet.clear();
      return parseFields(body.split("; "), data);
    }

    else {
      Matcher match = MASTER.matcher(body);
      if (!match.matches()) return false;

      setFieldList("ADDR APT CITY SRC CH INFO");
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(1).trim(), data);
      data.strSource = getOptGroup(match.group(2));
      data.strChannel = getOptGroup(match.group(3));
      String info = match.group(4).trim();
      if (!info.equals("None")) {
        data.strSupp = INFO_BRK_PTN.matcher(info).replaceAll("\n").trim();
      }
      return true;
    }
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC_CH")) return new MySourceChannelField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern SRC_CHANNEL_PTN = Pattern.compile("(.*) - (TAC\\d+)");
  private class MySourceChannelField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
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

  private static final Pattern INFO_PFX_PTN = Pattern.compile("(?:\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d [-\\*] *)+");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PFX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      if (field.equals("None")) return;
      super.parse(field, data);
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
