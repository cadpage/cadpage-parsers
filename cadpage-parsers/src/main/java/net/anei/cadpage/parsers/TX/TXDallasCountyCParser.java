package net.anei.cadpage.parsers.TX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXDallasCountyCParser extends DispatchOSSIParser {
  
  public TXDallasCountyCParser() {
    super(CITY_CODES, "DALLAS COUNTY", "TX",
          "( CANCEL ADDR CITY " +
          "| FYI ( CODE1 SRC! " +
                "| SRC UNIT? PLACE? CODE CALL X2+? ( ZERO | P ONE! ) " +
                "| ADDR PLACE1? ( CITY CITY2? | ) ( CODE1 SRC! " +
                                                 "| SRC MAP? UNIT? PLACE? CODE CALL X2+? ( ZERO | P ONE! ) " +
                                                 ") " +
                ") INFO+? MARK X2+? P ONE CITY? CALL BOX ID? " +
          ") INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ntecc.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
 
  
  private static final Pattern TRAIL_MARK_PTN = Pattern.compile("(.*\\])(.*;[P12](?:$|;1.*))");
  private static final Pattern STREET_APT_ADDR_PTN = Pattern.compile("(\\d+)-(\\d+) +(.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("FYI:") || subject.startsWith("Update:")) {
      body = subject + ';' + body;
    }
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    
    body = body.replace("\n", "");
    Matcher match = TRAIL_MARK_PTN.matcher(body);
    if (match.matches()) body = match.group(1)+"<<%MARK%>>;" + match.group(2);
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) {
      data.strCall = data.strCode;
      data.strCode = "";
    }
    
    // A leading nnn-nnn address might be a regular address range
    // but more often that not it will be address-apt combination.
    // See if we can figure them out
    match = STREET_APT_ADDR_PTN.matcher(data.strAddress);
    if (match.matches()) {
      String stno1 = match.group(1);
      String stno2 = match.group(2);
      if (stno1.length() != stno2.length() ||
          stno1.length() < 3 ||
          !stno1.substring(0,2).equals(stno2.substring(0,2))) {
        data.strApt = append(stno2, "-", data.strApt);
        data.strAddress = stno1 + ' ' + match.group(3);
      }
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE CALL?");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE1")) return new MyPlace1Field();
    if (name.equals("CITY2")) return new MyCity2Field();
    if (name.equals("CODE")) return new CodeField("[A-Z]{2,6}", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,4}", true);
    if (name.equals("MAP")) return new MapField("\\d{4}", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("ZERO")) return new SkipField("0", true);
    if (name.equals("P")) return new SkipField("[P12]", true);
    if (name.equals("ONE")) return new SkipField("1", true);
    if (name.equals("INFO")) return new MyInfoField();

    // Supporting obsolete format
    if (name.equals("CODE1")) return new MyCode1Field();
    if (name.equals("MARK")) return new SkipField("<<%MARK%>>");
    if (name.equals("BOX")) return new BoxField("\\d+", true);
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }
  
  private static final Pattern PLACE_PTN = Pattern.compile("\\(S\\) *(.*?) *\\(N\\)");
  private static final Pattern NOT_PLACE_PTN = Pattern.compile("[A-Z]{3,4}");
  private class MyPlace1Field extends PlaceField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = PLACE_PTN.matcher(field);
      if (match.matches()) {
        data.strPlace = match.group(1);
        return true;
      }
      
      if (NOT_PLACE_PTN.matcher(field).matches()) return false;
      data.strPlace = field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyCity2Field extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(getRelativeField(-1));
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+\\d+|.*,.*");
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      setPattern(UNIT_PTN, true);
    }
  }


  private static final Pattern CROSS_TAC_PTN = Pattern.compile("TAC *\\S*");
  private class MyCross2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      if (CROSS_TAC_PTN.matcher(field).matches()) {
        data.strChannel = field;
        return;
      }
      
      if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = append(data.strUnit, ",", field);
        return;
      }
      data.strCross = append(data.strCross, "/", field);
    }

    @Override
    public String getFieldNames() {
      return "X CH UNIT";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (CROSS_TAC_PTN.matcher(field).matches()) {
        data.strChannel = field;
      } else if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH? INFO";
    }
  }
  
  private class MyCode1Field extends CodeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CODE_SET.contains(field)) return false;
      data.strCode = field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equals("DFW AIRPORT")) city = "DALLAS/FORT WORTH AIRPORT";
    return super.adjustMapCity(city);
  }
  
  // This is only needed for historical calls, so we don't have to worry about it
  // being incomplete
  private static final Set<String> CODE_SET = new HashSet<String>(Arrays.asList(
      "CFA",
      "FFA",
      "GF",
      "HAZCON",
      "JAILME",
      "MAJOR",
      "MA",
      "ME",
      "MUTUAL",
      "PUBLIC",
      "RFA",
      "SF",
      "UNLK",
      "UNLKEM"
  ));
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ADDI", "ADDISON",
      "CARR", "CARROLLTON",
      "COPP", "COPPELL",
      "DALL", "DALLAS",
      "DECO", "DENTON COUNTY",
      "DFW",  "DFW AIRPORT",
      "FARM", "FARMERS BRANCH",
      "FLOW", "FLOWER MOUND",
      "FRIS", "FRISCO",
      "GRAP", "GRAPEVINE",
      "HEBR", "HEBRON",
      "IRVI", "IRVING",
      "LEWI", "LEWISVILLE",
      "PLAN", "PLANO",
      "PRI",  "PRINCETON",
      "THEC", "THE COLONY"
  });

}
