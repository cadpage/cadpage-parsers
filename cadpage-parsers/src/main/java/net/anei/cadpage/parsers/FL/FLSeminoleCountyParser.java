package net.anei.cadpage.parsers.FL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;

public class FLSeminoleCountyParser extends FieldProgramParser {

  public FLSeminoleCountyParser() {
    super("SEMINOLE COUNTY", "FL",
          "( ID STATUS CODE_CALL ( MASH END " +
                                "| ADDR1 CITY_X1 CITY! " +
                                "| ADDR CITY_X PLACE! " +
                                ") " +
          "| ADDR1/SC CITY_X1 CITY! " +
          "| ADDR/SC CITY_X PLACE! " +
          ") EXTRA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MW_STREET_LIST);
  }

  private static final Pattern MASTER = Pattern.compile("([^/]+)/(.*?);(.*?) (?:: (.*?) )?TAC:(.*) UNITS:(.*)");
  private static final Pattern MSPACE_PTN = Pattern.compile(" +");

  protected boolean parseMsg(String body, Data data) {

    String[] flds = splitFields(body);
    if (flds.length > 1) return parseFields(splitFields(body), data);

    // Special mutual aid page format
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    setFieldList("CALL ADDR APT X CITY MAP PLACE CH UNIT");

    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, match.group(1).trim(), data);
    data.strCross = match.group(2).trim();
    String extra = match.group(3).trim();
    data.strPlace = getOptGroup(match.group(4));
    data.strChannel = match.group(5).trim();
    data.strUnit = match.group(6).trim();

    Set<String> notMapSet = new HashSet<>();
    notMapSet.add("MP");
    notMapSet.add("MAP");
    for (String part : MSPACE_PTN.split(data.strChannel)) notMapSet.add(part);
    for (String part : MSPACE_PTN.split(data.strUnit)) notMapSet.add(part);

    for (String part : MSPACE_PTN.split(extra)) {
      if (!notMapSet.contains(part)) {
        String city = CITY_CODES.getProperty(part);
        if (city != null) {
          data.strCity = city;
        } else {
          data.strMap = append(data.strMap, " ", part);
        }
      }
    }
    return true;
  }

  private String[] splitFields(String body) {
    ArrayList<String> fields = new ArrayList<>();
    int st = 0;
    int lev = 0;
    for (int pt = 0; pt < body.length(); pt++) {
      char chr = body.charAt(pt);
      if (chr == '(') lev++;
      else if (chr == ')') lev--;
      else if (lev == 0) {
        if (chr == '\n' || chr == ',') {
          fields.add(body.substring(st, pt).trim());
          st = pt+1;
        }
      }
    }
    fields.add(body.substring(st).trim());
    return fields.toArray(new String[fields.size()]);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("CITY_X1")) return new MyCityCross1Field();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ID")) return new IdField("[A-Z]{2,3}-\\d{4}-\\d{2}-\\d{4}", true);
    if (name.equals("STATUS")) return new SkipField("DISPATCHED|ENROUTE|ON SCENE|IN COMMAND|TERMINATE COMMAND|AT FHA", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("CITY_X")) return new MyCityCrossField();
    if (name.equals("PLACE")) return new PlaceField("\\( *(.*?) *\\)", true);
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("MASH")) return new MyMashField();
    return super.getField(name);
  }

  private class MyAddress1Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.endsWith(" ( )")) return false;
      field = field.substring(0, field.length()-4).trim();
      super.parse(field, data);;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern CITY_X1_PTN = Pattern.compile("([ A-Z]+) / +(.*) \\( \\)");
  private class MyCityCross1Field extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_X1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = match.group(1).trim();
      data.strAddress = append(data.strAddress, " & ", match.group(2).trim());
    }

    @Override
    public String getFieldNames() {
      return "CITY ADDR";
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)- +(.*)");

  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }

  }

  private static final Pattern FLD_PAREN_PTN = Pattern.compile("([^#]*?)(?:#(.*?))?\\((.*)\\)");

  private class MyCityCrossField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = FLD_PAREN_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = match.group(1).trim();
      data.strApt = append(data.strApt, "-", getOptGroup(match.group(2)));
      data.strCross = match.group(3).trim();
    }

    @Override
    public String getFieldNames() {
      return "CITY X";
    }
  }

  private class MyExtraField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("(")) abort();
      int pt = field.indexOf(')');
      if (pt < 0) abort();
      String map = field.substring(1, pt).trim();
      field = field.substring(pt+1).trim();
      data.strMap = stripFieldStart(map, ";");

      Parser p = new Parser(field);
      data.strUnit = p.getLastOptional("UNITS:");
      data.strChannel = p.getLastOptional("TAC:");
      data.strPlace = append(data.strPlace, " - ", stripFieldStart(p.get(), ":"));
    }

    @Override
    public String getFieldNames() {
      return "MAP PLACE CH UNIT";
    }
  }

  private static final Pattern MASH_PTN = Pattern.compile("([^/]+)/(.*?);(.*?)(?:: (.*?) )?");
  private static final Pattern MASH_CH_PTN = Pattern.compile("\\d{2}[A-Z]|F\\d|\\d+TAC\\d+");

  private class MyMashField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MASH_PTN.matcher(field);
      if (!match.matches()) return false;
      parseAddress(match.group(1).trim(), data);
      data.strCross = match.group(2).trim();
      data.strPlace = getOptGroup(match.group(4));

      int typeCode = 1;
      for (String part : MSPACE_PTN.split(match.group(3).trim())) {
        if (typeCode == 1) {
          String city = CITY_CODES.getProperty(part);
          if (city != null) {
            data.strCity = city;
            continue;
          }
        }
        if (MASH_CH_PTN.matcher(part).matches()) typeCode = 3;
        else if (part.equals("MP") || part.equals("MAP")) {
          typeCode = 2;
          continue;
        }
        switch (typeCode) {
        case 1:
          data.strUnit = append(data.strUnit, " ", part);
          continue;

        case 2:
          data.strMap = append(data.strMap, " ", part);
          continue;

        case 3:
          data.strChannel = append(data.strChannel, " ", part);
          continue;
        }
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT X CITY UNIT MAP CH";
    }
  }

  private static Pattern SHADY_HOLW_PTN = Pattern.compile("\\bSHADY +HOLW\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String sAddress) {
    return SHADY_HOLW_PTN.matcher(sAddress).replaceAll("SHADY HOLLOW LN");
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "2ND ALARM - STRUCTURE FIRE RESIDENTIAL",
      "ALERT 1A",
      "ABDOMINAL PAIN - FAINTING OR NEAR FAINTING >=50",
      "ANIMAL BITES",
      "ASSAULT - OVERRIDE",
      "BACK PAIN - OVERRIDE",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "CARBON MONOXIDE / INHALATION / HAZMAT",
      "CARDIAC ARREST",
      "COMMAND ESTABLISHED NATURAL / PROPANE GAS LEAK OUTSIDE",
      "COMMAND ESTABLISHED REF NATURAL / PROPANE GAS LEAK INSIDE",
      "COMMAND ESTABLISHED REF STRUCTURE FIRE RESIDENTIAL",
      "COMMAND ESTABLISHED REF TRAFFIC / TRANSPORTATION ACCIDENTS",
      "COMMAND ESTABLISHED REF TRAFFIC ACCIDENT (ENTRAPMENT)",
      "COMMAND ESTABLISHED SMOKE IN STRUCTURE COMMERICAL",
      "DIABETIC PROBLEMS",
      "EMDA",
      "EMDB",
      "EMDD",
      "FALLS",
      "FALLS - NOT ALERT",
      "GAS EXPLOSION",
      "GENERAL ALERT",
      "HAZARDOUS CONDITION",
      "HEADACHE",
      "HEART PROBLEMS",
      "ILLEGAL BURN",
      "MECHANICAL ALARM COMMERCIAL",
      "MECHANICAL ALARM RESIDENTIAL",
      "NATURAL / PROPANE GAS LEAK INSIDE",
      "NATURAL / PROPANE GAS LEAK OUTSIDE",
      "OVEN FIRE (CONTAINED)",
      "OVERDOSE / POISONING",
      "OVERDOSE / POISONING - OVERRIDE",
      "PSYCHIATRIC",
      "PREGNANCY / CHILDBIRTH",
      "PUBLIC ASSIST",
      "SICK PERSON",
      "SMOKE IN STRUCTURE COMMERCIAL",
      "SMOKE IN STRUCTURE RESIDENTIAL",
      "SMOKE ODOR IN STRUCTURE",
      "STAB / GUNSHOT / PENETRATING TRAUMA",
      "STAB / GUNSHOT / PENETRATING TRAUMA - OVERRIDE-GUNSHOT",
      "STAB / GUNSHOT",
      "STRUCTURE FIRE COMMERCIAL",
      "STRUCTURE FIRE RESIDENTIAL",
      "TRAFFIC / TRANSPORTATION ACCIDENTS",
      "TRAFFIC ACCIDENT",
      "TRAFFIC ACCIDENT - PINNED (TRAPPED) VICTIM",
      "TRAFFIC ACCIDENT (ENTRAPMENT)",
      "TRAFFIC ACCIDENT (SUBMERGED)",
      "TRAFFIC ACCIDENT (VEH VS STRUCTURE)",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS / FAINTING",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "UNKNOWN PROBLEM (PERSON DOWN)",
      "UPDATE:  COMMAND TERMINATED - E16 ADVISED LOG IN FIREPLACE REF SMOKE IN STRUCTURE COMMERICAL",
      "WATER FLOW ALARM",
      "WORKING STRUCTURE FIRE RESIDENTIAL"
   );

  private static final String[] MW_STREET_LIST = new String[] {
    "BRANTLEY HALL",
    "COTTONWOOD CREEK",
    "CONTROL TOWER",
    "GOLDEN DAYS",
    "GRASSY POINT",
    "HIDDEN MEADOWS",
    "HITCHING POST",
    "HERITAGE PARK",
    "HOWELL BRANCH",
    "LAKE EMMA",
    "LAKE HOWELL",
    "LAKE MARY",
    "LONGWOOD LAKE MARY",
    "POINTE NEWPORT",
    "QUEENS MIRROR",
    "RED BUG LAKE",
    "REGAL POINTE",
    "ROSE MALLOW",
    "SABAL LAKE",
    "SHADY HOLW", //the weird spelling is consistent even between departments
    "SPANISH TRACE",
    "ST JOHNS",
    "WILLA SPRINGS",
    "WINDSOR CRESCENT",
    "WINDING LAKE",
    "WINTER GREEN",
    "WINTER PARK",
    "VILLAGE OAK"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "OCJZ", "ORANGE COUNTY",
      "VCJZ", "VOLUSIA COUNTY",
      "WPJZ", "WINTER PARK"
  });
}
