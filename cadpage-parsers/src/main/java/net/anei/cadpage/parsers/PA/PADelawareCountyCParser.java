package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PADelawareCountyCParser extends FieldProgramParser {
  
  public PADelawareCountyCParser() {
    super("DELAWARE COUNTY", "PA",
           "SKIP? ID DATE TIME CALL CALL ADDR! X X INFO+");
  }
  
  @Override
  public String getFilter() {
    return "norwoodfireco@rcn.com,dispatch@smtp-auth.no-ip.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), 6, data)) return false;
    
    // It is not uncommon to report the same info in address and cross street fields :(
    if (data.strCross.equals(data.strAddress)) data.strCross = "";
    
    // Less common, but more annoying, address contains a street name, and cross street
    // contains the intersection with the first street name matching the address.  In which 
    // case the cross street should be used as the address
    if (data.strCross.startsWith(data.strAddress)) {
      data.strAddress = data.strCross;
      data.strCross = "";
    }
    return true;
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private static final Pattern TRASH_PTN = Pattern.compile(",[^\\p{Print}].*$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = TRASH_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("F\\d{8}", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
