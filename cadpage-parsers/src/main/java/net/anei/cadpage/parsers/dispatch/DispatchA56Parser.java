package net.anei.cadpage.parsers.dispatch;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA56Parser extends FieldProgramParser {

  public DispatchA56Parser(String defCity, String defState) {
    super(defCity, defState, 
          "UNIT ( DATETIME CODECALL LOCATION APT? PLACE? UNIT! | CODECALL LOCATION! ) INFO+");
  }
  
  private static Pattern MASTER1 = Pattern.compile("DISPATCH:(.*?) - (\\d{2}/\\d{2} \\d{2}:\\d{2}) - (.+)", Pattern.DOTALL);
  private static Pattern MASTER2 = Pattern.compile("DISPATCH:([-A-Z0-9: ]+?(?: Dispatch)?) - ([- :A-Z0-9]+)/(.+)", Pattern.DOTALL);
  private static final Pattern DELIM = Pattern.compile("\n\n|//");
  
  private Set<String> unitSet = new HashSet<String>();
  
  @Override
  public boolean parseMsg(String body, Data data) {

    //split first field into smaller ones
    Matcher mat = MASTER1.matcher(body);
    if (!mat.matches()) {
      mat = MASTER2.matcher(body);
      if (!mat.matches()) return false;
    }
    body = mat.group(1) + "//" + mat.group(2) + "//" + mat.group(3);
    
    unitSet.clear();
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("(\\d{2}/\\d{2} \\d{2}:\\d{2})", true);
    if (name.equals("CODECALL")) return new BaseCodeCallField();
    if (name.equals("LOCATION")) return new BaseLocField();
    if (name.equals("APT")) return new AptField("\\d{1,4}[A-Z]?|[A-Z]", true);
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("UNIT")) return new BaseUnitField();
    return super.getField(name);
  }
  
  private static final Pattern ID_CALL = Pattern.compile("([ A-Z]+):(\\d{2}-\\d{4,})\\s+(.*)");
  private static final Pattern CODE_CALL = Pattern.compile("(\\d+)\\s+(.+)");
  private class BaseCodeCallField extends Field {
    @Override public void parse(String field, Data data) {
      
      // Extract source and call ID
      Matcher mat = ID_CALL.matcher(field);
      if (mat.matches()) {
        if (data.strSource.length() == 0) data.strSource = mat.group(1).trim();
        data.strCallId = mat.group(2);
        field = mat.group(3);
      }
      
      //numeric CODE precedes CALL
      mat = CODE_CALL.matcher(field);
      if (mat.matches()) {
        data.strCode = mat.group(1);
        data.strCall = mat.group(2);
      } else {
        data.strCall = field;
      }
    }

    @Override public String getFieldNames() {
      return "SRC ID CODE CALL";
    }
  }
  
  private static final Pattern EXTRA_COMMA_PTN = Pattern.compile(" *, *");
  private class BaseLocField extends AddressField {
    @Override public void parse(String field, Data data) {
      Parser p = new Parser(field);
      //last field is always city
      data.strCity = p.getLastOptional(',');
      //second to last is sometimes APT
      data.strApt = p.getLastOptional("Apt/Unit #");
      //parse the rest normally
      field = EXTRA_COMMA_PTN.matcher(p.get()).replaceAll(" ").trim();
      super.parse(field, data);
    }

    @Override public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class BasePlaceField extends PlaceField {
    @Override public void parse(String field, Data data) {
      field = stripFieldStart(field, "ALIAS=");
      super.parse(field, data);
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("([A-Z0-9 ]+:[-A-Z0-9 ]+?)");
  private static final Pattern UNIT_PTN2 = Pattern.compile("[-A-Z0-9: ]+");
  private class BaseUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String[] units = field.split(",");
      for (int j = 0; j<units.length; j++) {
        String unit = units[j];
        int pt = unit.lastIndexOf(' ');
        if (pt >= 0) {
          if ("DISPATCH".startsWith(unit.substring(pt+1).toUpperCase())) {
            unit = unit.substring(0,pt).trim();
          }
        }
        
        // Relax rules for last unit in last field that might have been truncated
        Pattern unitPtn = (j == 0 || j != units.length-1 || !isLastField()) ? UNIT_PTN : UNIT_PTN2;
        if (!unitPtn.matcher(unit).matches()) 
          return false;

        unit = unit.trim().replace(' ', '_');
        if (unitSet.add(unit)) data.strUnit = append(data.strUnit, ",", unit);
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  

}
