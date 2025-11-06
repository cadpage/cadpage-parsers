package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHCarrollCountyBParser extends FieldProgramParser {
  
  public OHCarrollCountyBParser() {
    super("CARROLL COUNTY", "OH", 
          "CALL ( GPS1 GPS2! | ADDR! CITY ST ) FAIL XST:X! NOTES:INFO INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "idcad@carrollcountysheriff.org";
  }

  private static final Pattern LEAD_SRC_PTN = Pattern.compile("(\\d{2}): +");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = LEAD_SRC_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strSource = match.group(1);
    body = body.substring(match.end());
    
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split(", "), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ST")) return new StateField("([A-Z]{2})(?: +\\d{5})?");
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      String city = ' ' + data.strCity;
      field = stripFieldEnd(field, city);
      field = field.replace(city+'/', "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
