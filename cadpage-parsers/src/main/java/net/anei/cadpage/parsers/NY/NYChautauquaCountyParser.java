package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYChautauquaCountyParser extends FieldProgramParser {
  
  private static final Pattern MARKER1 = Pattern.compile("^CHAUTAUQUA_COUNTY_SHERIFF:? \\(([A-Z ]+)\\) +");
  private static final Pattern MARKER2 = Pattern.compile("^(\\d\\d:\\d\\d)[ ;]+");
  private static final Pattern MASTER1 = Pattern.compile("([ A-Z0-9]+?)  +([A-Z]+\\d+) +(.*) +\\*([ A-Z0-9]+) (\\d{4}-\\d{8})");
  private static final Pattern DELIM = Pattern.compile(" *(?<= ); | +\\n");
  private static final Pattern NOT_APT_PTN = Pattern.compile("CSX.*");
  
  public NYChautauquaCountyParser() {
    super(CITY_LIST, "CHAUTAUQUA COUNTY", "NY",
          "CALL! PLACE? ADDR/ZiSXa! C/T/V:CITY! UNIT1? INFO+? UNIT2 X-Streets:X END");
  }
  
  @Override
  public String getFilter() {
    return "911@cattco.org,777,888,messaging@iamresponding.com,dispatch@sheriff.us,2183150154,4702193663";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return RT_20_PTN.matcher(addr).replaceAll("US 20");
  }
  private static final Pattern RT_20_PTN = Pattern.compile("(?<!OLD )\\bRT *20\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MARKER1.matcher(body);
    if (match.find()) {
      data.strSource = match.group(1);
      body = body.substring(match.end());
    }
    
    else {
      if (subject.length() == 0) return false;
      data.strSource = subject;
    }
    
    match = MARKER2.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      body = body.substring(match.end()).trim();
    }
    
    if (body.startsWith("New Call")) body = '*' + body;
    if (body.startsWith("*") || body.contains("\n")) {
      body = stripFieldStart(body, "*");
      body = body.replace("C/T/V ", "C/T/V:");
      body = stripFieldEnd(body, ";");
      if (!parseFields(DELIM.split(body),data)) return false;
      if (data.strApt.length() > 0 && NOT_APT_PTN.matcher(data.strApt).matches()) {
        data.strAddress = append(data.strAddress, " & ", data.strApt);
        data.strApt = "";
      }
      return true;
    }
    
    match = MASTER1.matcher(body);
    if (!match.matches()) return false;
    setFieldList("UNIT ADDR APT PLACE CITY CALL ID");
    data.strUnit = match.group(1) + " " + match.group(2);
    parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD | FLAG_ANCHOR_END, match.group(3).trim(), data);
    data.strPlace = getPadField();
    data.strCall = match.group(4).trim();
    data.strCallId = match.group(5);
    return true;
 }
  
  @Override
  public String getProgram() {
    return "SRC TIME " + super.getProgram();
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("UNIT1")) return new MyUnitField(1);
    if (name.equals("UNIT2")) return new MyUnitField(2);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  // City field must remove trailing _T/V/C
  private static final Pattern CITY_SFX_PTN = Pattern.compile("(.*)_[TVC]");
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_SFX_PTN.matcher(field);
      if (match.matches()) field = match.group(1).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern[] UNIT_PTN_LIST = new Pattern[]{
    Pattern.compile("[A-Z]+\\d+"),
    Pattern.compile("(?: *\\b(?:[A-Z]{1,4}\\d*)\\b)+")
  };

  private class MyUnitField extends UnitField {
    
    public MyUnitField(int type) {
      setPattern(UNIT_PTN_LIST[type-1], true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities",
    "DUNKIRK",
    "JAMESTOWN",

    // Towns
    "ARKWRIGHT",
    "BUSTI",
    "CARROLL",
    "CHARLOTTE",
    "CHAUTAUQUA",
    "CHERRY CREEK",
    "CLYMER",
    "DUNKIRK",
    "ELLERY",
    "ELLICOTT",
    "ELLINGTON",
    "FRENCH CREEK",
    "GERRY",
    "HANOVER",
    "HARMONY",
    "KIANTONE",
    "MINA",
    "NORTH HARMONY",
    "POLAND",
    "POMFRET",
    "PORTLAND",
    "RIPLEY",
    "SHERIDAN",
    "SHERMAN",
    "STOCKTON",
    "VILLENOVA",
    "WESTFIELD",

    // Villages
    "BEMUS POINT",
    "BROCTON",
    "CASSADAGA",
    "CELORON",
    "CHERRY CREEK",
    "FALCONER",
    "FORESTVILLE",
    "FREDONIA",
    "LAKEWOOD",
    "MAYVILLE",
    "PANAMA",
    "SHERMAN",
    "SILVER CREEK",
    "SINCLAIRVILLE",
    "WESTFIELD",

    // Hamlets",
    "ASHVILLE",
    "BUSTI",
    "FINDLEY LAKE",
    "FREWSBURG",
    "HAMLET",
    "IRVING",
    "KENNEDY",
    "LAONA",
    "LILY DALE",
    "MAPLE SPRINGS"
  };
}
	