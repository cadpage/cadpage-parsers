package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

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
    return "llewellynscanner@hotmail.com,schuylkill.paging@gmail.com,good_intent@comcast.net,citizens65fc@gmail.com,pocsagpaging@comcast.net,Engine369@ptd.net,smf@schmobile.com,webfiredispatch@gmail.com,tslane@ptd.net,lt532@comcast.nets,daveyp@comcast.net,webfiredispatch@goodintentfire.com,wpfc37.relay@gmail.com,mcadoo.ems.alert@gmail.com,stclairems911@comcast.net";
  }

  private static final Pattern PREFIX_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) (?:\\d\\d-\\d\\d-\\d\\d )?(?: ([A-Z]+)  )? *");
  private static final Pattern SRC_PTN = Pattern.compile("(.*) - ([A-Z]*[a-z][A-Za-z ]*\\d*|[A-Z][A-Z ]+(?:FIRE|FC|EMS))", Pattern.DOTALL);
  private static final Pattern MISSING_BREAK_PTN = Pattern.compile(" (?=FOR A:|TRUCKS:|TIME:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = PREFIX_PTN.matcher(body);
    String time = null;
    if (match.lookingAt()) {
      time = match.group(1);
      data.strPriority = getOptGroup(match.group(2));
      body = body.substring(match.end());
    }
    
    body = stripFieldStart(body, "REPAGE ");
    
    int pt = body.indexOf("\n\n---\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    match = SRC_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strSource = match.group(2);
    }
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
  private static final Pattern ADDR_INTERSECT_PTN = Pattern.compile("(.*?)-(\\d\\d)/(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("1 THE ROCK-SCHUYLKILL BERKS LINE")) {
        data.strAddress = field;
        return;
      }
      field = ADDR_TWSP_PTN.matcher(field).replaceAll("TWP");
      Matcher match = ADDR_INTERSECT_PTN.matcher(field); 
      if (match.matches()) {
        parseAddress(match.group(1).trim() + " & " + match.group(3).trim(), data);
        data.strCity = convertCodes(match.group(2), CITY_CODES);
      }
      
      else {
        int pt = field.lastIndexOf('-');
        if (pt < 0) abort();
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field.substring(0,pt).trim(), data);
        String city = field.substring(pt+1);
        if (city.length() > 0) {
          if (city.startsWith(" ")) {
            data.strApt = city.trim();
          } else {
            String saveCity = data.strCity;
            parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
            if (data.strCity.length() == 0) abort();
            data.strApt = getLeft();
            if (saveCity.length() > 0) data.strCity = saveCity;
            data.strCity = convertCodes(data.strCity.toUpperCase(), CITY_ABBRV);
          }
        }
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
    "POTTSVILLE",

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
    "HUBLEY TWP",
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
    "UPPER MAHANTONGO TWP",
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
    "SEEK",
    "SOUTH TAMAQUA",
    "STILL CREEK",
    "WEISHAMPLE",
    
    // Other
    "FAIRLANE MALL",
    "SCH MALL",
    
    // Berks County
    "BERKS",
    "HAMBURG",
    "UPPER TOPAHOCHEN TWP",
    
    // Carbon County
    "BANKS TWP",
    "LEHIGH TWP",
    
    // Dauphin County
    "DAUPHIN COUNTY",
    "GRATZ",
    
    // Luzerne County
    "HAZLE TWP",
    
    // Northumberland County
    "MT CARMEL",
    "MT CARMEL TWP"
  };
  
  private static final Properties CITY_ABBRV = buildCodeTable(new String[]{
      "BERKS",        "BERKS COUNTY",
      "SCH MALL",     "SCHUYLKILL MALL",
      "MC ADOO",      "MCADOO",
      "UPPER MAH",    "UPPER MAHANTONGO TWP"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "03", "BRANCH TWP",
      "11", "FOSTER TWP",
      "20", "MINERSVILLE",
      "22", "PORTER TWP",
      "33", "WASHINGTON TWP"
  });
}
