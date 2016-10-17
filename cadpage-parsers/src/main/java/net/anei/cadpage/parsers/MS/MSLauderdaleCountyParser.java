package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MSLauderdaleCountyParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("\\((\\d+)\\) *([A-Z]+)");

  public MSLauderdaleCountyParser() {
    super(CITY_LIST, "LAUDERDALE COUNTY", "MS",
          "CALL ( ADDR/Z CITY | ADDR/Z ADDR/Z CITY | ADDR/Z ADDR/Z ADDR/Z CITY | ADDR/Z ) INFO/N+? UNIT SRC! END");
  }
  
  @Override
  public String getFilter() {
    return "E911@meridianms.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    
    if (!parseFields(body.split("-"), data)) return false;
    String addr = data.strAddress;
    data.strAddress = "";
    parseAddress(addr, data);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("SRC")) return new SourceField("[A-Z]", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, "-", field);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldEnd(addr, " INTERSECTN");
    return super.adjustMapAddress(addr);
  }
  
  private static final String[] CITY_LIST = new String[]{

      // Cities
      "MERIDIAN",
      
      // Town
      "MARION",
      
      // Census designated places
      "COLLINSVILLE",
      "LAUDERDALE",
      "MERIDIAN STATION",
      "NELLIEBURG",
      "TOOMSUBA",
      
      // Unincorporated communities
      "BAILEY",
      "DALEVILLE",
      "KEWANEE",
      "RUSSELL",
      "WHYNOT",
      "ZERO"
  };
}
