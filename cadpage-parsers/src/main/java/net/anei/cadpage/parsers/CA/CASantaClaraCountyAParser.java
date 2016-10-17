package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Santa Clara County, CA
 */
public class CASantaClaraCountyAParser extends FieldProgramParser {

  private static final Pattern MARKER = Pattern.compile("^([A-Z0-9 ]{4})#(M\\d{9})");
  
  public CASantaClaraCountyAParser() {
    super(CITY_CODES, "SANTA CLARA COUNTY", "CA",
           "LOC:ADDR! CTY:CITY! ADDL:PLACE! TYPE:CALL! CD:PRI! MAP:MAP! DETAILS:INFO! TC:DATETIME!");
    setBreakChar('=');
  }
  
  @Override
  public String getFilter() {
    return "cademail@911.sccgov.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strSource = match.group(1).trim();
    data.strCallId = match.group(2);
    body = body.substring(match.end()).trim();
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_PAREN_PTN = Pattern.compile(" *\\([^\\(\\)]*\\) *");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      field = field.replace('@', '&');
      field = ADDR_PAREN_PTN.matcher(field).replaceAll(" ").trim().replaceAll("  +", " ");
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field,  "@");
      if (field.equals("NONE")) return;
      super.parse(field, data);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/NONE");
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = CL_PTN.matcher(addr).replaceAll("CIR");
    addr = MC_PTN.matcher(addr).replaceAll("MC");
    addr = EX_PTN.matcher(addr).replaceAll("EXPY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern CL_PTN = Pattern.compile("\\bCL\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern MC_PTN = Pattern.compile("\\bMC ", Pattern.CASE_INSENSITIVE);
  private static final Pattern EX_PTN = Pattern.compile("\\bEX\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CO", "",               // County??
      "CU", "CUPERTINO",
      "GI", "GILROY",
      "MH", "MORGAN HILL",
      "SC", "SANTA CLARA",
      "SJ", "SAN JOSE" 
  });
}
