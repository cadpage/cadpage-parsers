package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Schuylkill County, PA
 */
public class PASchuylkillCountyParser extends FieldProgramParser {

  public PASchuylkillCountyParser() {
    super(CITY_LIST, "SCHUYLKILL COUNTY", "PA",
          "RESPOND_TO:ADDRCITY! FOR_A:CODE_CALL! OPS_CHNL:CH? TIME:TIME? TRUCKS:UNIT CN:PLACE INFO+");
  }

  @Override
  public String getFilter() {
    return "llewellynscanner@hotmail.com,schuylkill.paging@gmail.com,good_intent@comcast.net,citizens65fc@gmail.com,pocsagpaging@comcast.net,Engine369@ptd.net,smf@schmobile.com,webfiredispatch@gmail.com,tslane@ptd.net,lt532@comcast.nets,daveyp@comcast.net,webfiredispatch@goodintentfire.com,wpfc37.relay@gmail.com,mcadoo.ems.alert@gmail.com,stclairems911@comcast.net,gifc.active911@comcast.net,gwfc6099@verizon.net,messaging@iamresponding.com,lieutenant701@ptd.net";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean splitKeepLeadBreak() { return true; }
      @Override public boolean mixedMsgOrder() { return true; }
    };
  }

  private static final Pattern PREFIX_PTN1 = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) (?:\\d\\d-\\d\\d-\\d\\d )?(?: ([A-Z]+)  )? *");
  private static final Pattern PREFIX_PTN2 = Pattern.compile("(\\d{7}) +");
  private static final Pattern REPAGE_PTN = Pattern.compile("REPAGE[ \\.]+");
  private static final Pattern SRC_PTN = Pattern.compile("(.*) - ([A-Z]*[a-z][A-Za-z ]*\\d*|[A-Z][A-Z ]+(?:FIRE|FC|EMS))", Pattern.DOTALL);
  private static final Pattern MISSING_BREAK_PTN = Pattern.compile(" (?=FOR A:|TRUCKS:|TIME:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = body.indexOf("\n\n---\n");
    if (pt >= 0) {
      String tail = body.substring(pt);
      body = body.substring(0,pt).trim();
      if (isMultiMsg()) {
        pt = body.indexOf(tail);
        if (pt >= 0) {
          body = body.substring(0,pt) + body.substring(pt+tail.length());
        }
      }
    }

    if (!isMultiMsg() && body.length() >= 238) data.expectMore = true;

    Matcher match = PREFIX_PTN1.matcher(body);
    String time = null;
    if (match.lookingAt()) {
      time = match.group(1);
      data.strPriority = getOptGroup(match.group(2));
      body = body.substring(match.end());
    }
    else if ((match = PREFIX_PTN2.matcher(body)).lookingAt()) {
      data.strSource = match.group(1);
      body = body.substring(match.end());
    }

    match = REPAGE_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());

    match = SRC_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strSource = match.group(2);
    }
    body = body.replace("FOR \nA:", "FOR A:");
    body = MISSING_BREAK_PTN.matcher(body).replaceAll("\n");
    if (!super.parseFields(body.split("\n"), data)) return false;
    if (time != null) data.strTime = time;
    return true;
  }

  @Override
  public String getProgram() {
    String prog = "PRI " + super.getProgram() + " SRC";
    if (prog.indexOf("TIME") < 0) prog = "TIME " + prog;
    return prog;
  }


  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }

  private static final Pattern ADDR_TWSP_PTN = Pattern.compile("\\bTWSP\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_INTERSECT_PTN = Pattern.compile("(.*?)-(\\d\\d)(?:_HOLD)?/(.*)");
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?)-(\\d\\d) (\\(.*\\))");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("1 THE ROCK-SCHUYLKILL BERKS LINE")) {
        data.strAddress = field;
        return;
      }

      field = ADDR_TWSP_PTN.matcher(field).replaceAll("TWP");
      Matcher match = ADDR_INTERSECT_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim() + " & " + match.group(3).trim(), data);
        data.strCity = convertCodes(match.group(2), CITY_CODES);
      }

      else if ((match = ADDR_CITY_PTN.matcher(field)).matches()) {
        parseAddress(match.group(1).trim() + " " + match.group(3), data);
        data.strCity = convertCodes(match.group(2), CITY_CODES);
      }

      else {
        boolean found = false;
        String city = "";
        String apt = "";
        int pt = field.length();
        while (true) {
          if (pt <= 0) break;
          pt = field.lastIndexOf('-', pt-1);
          if (pt < 0) break;
          String tmp = field.substring(pt+1);
          if (tmp.length() > 0) {
            if (tmp.startsWith(" ")) {
              data.strApt = append(tmp.trim(), "-", apt);
            } else {
              parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, tmp, data);
              if (data.strCity.length() == 0) continue;
              apt = append(getLeft(), "-", apt);
              city = convertCodes(data.strCity.toUpperCase(), CITY_ABBRV);
              data.strCity = "";
            }
          }
          found = true;
          field = field.substring(0,pt).trim();
        }
        if (!found) abort();
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
        data.strApt = append(data.strApt, "-", apt);
        if (data.strCity.length() == 0) data.strCity = city;
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private class MyCodeCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt >= 0) {
        data.strCode = field.substring(0,pt).trim();
        data.strCall = field.substring(pt+1).trim();
      } else {
        data.strCall = data.strCode = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("(") && field.endsWith(")")) {
        field = field.substring(1,field.length()-1).trim();
      }
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[] {

    // Cities
    "PATTSVILLE",
    "POTTSVILLE",
    "POTTSVIL?E",
    "POTTSVILLE*",

    // Boroughs
    "ASHLAND",
    "AUBURN",
    "COALDALE",
    "CRESSONA",
    "DEER LAKE",
    "FRACKVILLE",
    "GILBERTON",
    "GIRARDVILLE",
    "GORDON",
    "LANDINGVILLE",
    "MAHANOY CITY",
    "MC ADOO",
    "MCADOO",
    "MECHANICSVILLE",
    "MIDDLEPORT",
    "MINERSVILLE",
    "MOUNT CARBON",
    "NEW PHILADELPHIA",
    "NEW RINGGOLD",
    "ORWIGSBURG",
    "PALO ALTO",
    "PINE GROVE",
    "PORT CARBON",
    "PORT CLINTON",
    "RINGTOWN",
    "SCHUYLKILL HAVEN",
    "SHENANDOAH",
    "SAINT CLAIR",
    "ST CLAIR",
    "TAMAQUA",
    "TOWER CITY",
    "TREMONT",

    //Townships
    "BARRY TWP",
    "BLYTHE TWP",
    "BRANCH TWP",
    "BUTLER TWP",
    "CASS TWP",
    "DELANO TWP",
    "EAST BRUNSWICK TWP",
    "EAST NORWEGIAN TWP",
    "EAST UNION TWP",
    "ELDRED TWP",
    "FOSTER TWP",
    "FRAILEY TWP",
    "HEGINS TWP",
    "HEGINSBTWP",
    "HUBLEY TWP",
    "HUBLEYBTWP",
    "HUHLEY TWP",
    "KLINE TWP",
    "MAHANOY TWP",
    "NEW CASTLE TWP",
    "NORTH MANHEIM TWP",
    "NORTH UNION TWP",
    "NORWEGIAN TWP",
    "PINE GROVE TWP",
    "PORTER TWP",
    "REILLY TWP",
    "RUSH TWP",
    "RYAN TWP",
    "SCHUYLKILL TWP",
    "SOUTH MANHEIM TWP",
    "TREMONT TWP",
    "UNION TWP",
    "UPPER MAH",
    "UPPER MAHANTONGH TWP",
    "UPPER MAHANTONGO TWP",
    "UPPER MAHANTONoO TWP",
    "WALKER TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",
    "WEST BRUNSWICK TWP",
    "WEST MAHANOY TWP",
    "WEST PENN TWP",

    // Census-designated places
    "ALTAMONT",
    "BEURYS LAKE",
    "BRANCHDALE",
    "BRANDONVILLE",
    "BUCK RUN",
    "CUMBOLA",
    "DELANO",
    "DONALDSON",
    "ENGLEWOOD",
    "FORRESTVILLE",
    "FOUNTAIN SPRINGS",
    "FRIEDENSBURG",
    "GRIER CITY",
    "HECKSCHERVILLE",
    "HEGINS",
    "HOMETOWN",
    "KELAYRES",
    "KLINGERSTOWN",
    "LAKE WYNONAH",
    "LAVELLE",
    "LOCUSTDALE",
    "MARLIN",
    "MCKEANSBURG",
    "MUIR",
    "NEWTOWN",
    "NUREMBERG",
    "ONEIDA",
    "ORWIN",
    "PARK CREST",
    "RAVINE",
    "REINERTON",
    "RENNINGERS",
    "SELTZER",
    "SHENANDOAH HEIGHTS",
    "SHEPPTON",
    "SUMMIT STATION",
    "TUSCARORA",
    "VALLEY VIEW",

    // Unincorporated communities
    "ANDREAS",
    "BROCKTON",
    "CONNERTON",
    "GINTHERS",
    "GOODSPRING",
    "HADDOCK",
    "HAUTO",
    "MANTZVILLE",
    "MAHONING VALLEY",
    "MARY D",
    "MOLINO",
    "ORWIN",
    "OWL CREEK",
    "PITMAN",
    "SEEK",
    "SOUTH TAMAQUA",
    "STILL CREEK",
    "WEISHAMPLE",

    // Other
    "FAIRLANE MALL",
    "SCH MALL",

    // Berks County
    "BERKS",
    "ALBANY TWP",
    "BETHEL TWP",
    "HAMBURG",
    "STRAUSSTOWN",
    "TILDEN TWP",
    "UPPER BERN TWP",
    "UPPER TULPEHOCKEN TWP",
    "WINDSOR TWP",

    // Carbon County
    "CARBON",
    "CARBON CO",
    "BANKS TWP",
    "BANKS TWP-CARBON CO",
    "BEAVER MEADOWS",
    "EAST PENN TWP",
    "LANSFORD",
    "LEHIGH TWP",
    "MAHONING TWP",
    "NEWQUEHONING",
    "PACKER TWP",
    "SUMMIT HILL",
    "TRESKOW",

    // Columbia County
    "COLUMBIA",
    "BEAVER TWP",
    "CENTRALIA",
    "CONYNGHAM TWP",
    "ROARING CREEK TWP",

    // Dauphin County
    "DAUPHIN COUNTY",
    "BERRYSBURG",
    "JACKSON TWP",
    "JEFFERSON TWP",
    "GRATZ",
    "LYKENS TWP",
    "RUSH TWP",
    "WILLIAMS TWP",
    "WILLIAMSTOWN",

    // Lebanon County
    "LEBANON",
    "COLD SPRING TWP",
    "UNION TWP",

    // Lehigh County
    "LEHIGH",
    "HEIDELBERG TWP",
    "LYNN TWP",

    // Luzerne County
    "LUZERNE",
    "BLACK CREEK TWP",
    "HAZLE TWP",

    // Northumberland County
    "NORTHUMBERLAND",
    "EAST CAMERON TWP",
    "KULPMONT",
    "MARION HEIGHTS",
    "MT CARMEL",
    "MT CARMEL TWP",
    "UPPER MAHANOY TWP"
  };

  private static final Properties CITY_ABBRV = buildCodeTable(new String[]{
      "BANKS TWP-CARBON CO", "BANKS TWP",
      "BERKS",        "BERKS COUNTY",
      "HEGINSBTWP",   "HEGINS TWP",
      "HUBLEYBTWP",   "HUBLEY TWP",
      "HUHLEY TWP",   "HUBLEY TWP",
      "MC ADOO",      "MCADOO",
      "PATTSVILLE",   "POTTSVILLE",
      "POTTSVIL?E",   "POTTSVILLE",
      "POTTSVILLE*",  "POTTSVILLE",
      "UPPER MAH",    "UPPER MAHANTONGO TWP",
      "UPPER MAHANTONGH TWP", "UPPER MAHANTONGO TWP",
      "UPPER MAHANTONoO TWP", "UPPER MAHANTONGO TWP",
      "SCH MALL",     "SCHUYLKILL MALL"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "01", "BARRY TWP",
      "02", "BLYTHE TWP",   //??
      "03", "BRANCH TWP",
      "04", "ASHLAND",
      "05", "POTTSVILLE",
      "06", "DELANO TWP",
      "07", "EAST BRUNSWICK TWP",
      "08", "POTTSVILLE",
      "10", "PITMAN",
      "11", "FOSTER TWP",
      "12", "PORTER TWP",
      "13", "HEGINS",
      "14", "HUBLEY TWP",
      "16", "MAHONEY TWP",
      "17", "POTTSVILLE",
      "18", "NORTH MANHEIM TWP",
      "20", "MINERSVILLE",
      "21", "PINE GROVE",
      "22", "PORTER TWP",
      "24", "REILY TWP",
      "25", "RUSH TWP",
      "26", "RYAN TWP",
      "27", "TAMAQUA",
      "28", "AUBURN",
      "29", "TREMONT TWP",
      "32", "WALKER TWP",
      "33", "WASHINGTON TWP",
      "34", "POTTSVILLE",
      "35", "ORWIGSBURG",
      "36", "WEST MAHONEY TWP",
      "37", "WEST PENN TWP",
      "41", "CRESSONA",
      "43", "FRACKVILLE",
      "52", "MINERSVILLE",
      "54", "BLYTHE TWP",
      "57", "POTTSVILLE",
      "58", "PINE GROVE",
      "62", "SAINT CLAIR",
      "63", "SCHUYLKILL HAVEN",
      "65", "TAMAQUA",
      "66", "TOWER CITY",
      "67", "TREMONT",
      "68", "POTTSVILLE"
  });
}
