package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Hanover County, VA
 */
public class VAHanoverCountyAParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[\\[\\(]?(\\d+)[\\)\\] a]*");
  private static final Pattern START_TRASH_PTN = Pattern.compile("^a +[\\)\n]");
    
  
  public VAHanoverCountyAParser() {
    super("HANOVER COUNTY", "VA",
           "Unit:UNIT! UnitSts:SKIP! Inc:CALL? Inc#:ID! Inc:CALL? MapRef:MAP? Loc:ADDR/SXI! MapRef:MAP Addtl:INFO");
  }
  
  @Override
  public String getFilter() {
    return "e911@ps400.hanoverva.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    
    match = START_TRASH_PTN.matcher(body);
    if (match.find()) body = body.substring(match.start()).trim();
    
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      
      // Incomplete intersections parser into the place field and need to be replaced
      if (data.strSupp.startsWith("/") || data.strSupp.startsWith("&")) {
        data.strAddress = data.strAddress + " & " + data.strSupp.substring(1).trim();
        data.strSupp = "";
      }
    }
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("^[A-Z] Map \\d{1,4}(?:-\\d)?\\b");
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.find()) abort();
      data.strMap = match.group();
      if (field.endsWith(",")) field = field.substring(0,field.length()-1).trim();
      field = field.substring(match.end()).trim();
      if (field.startsWith("APT")) {
        data.strApt = data.strApt = append(data.strApt, "-", field.substring(3).trim());
      } else {
        data.strSupp = append(data.strSupp, " / ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "MAP APT INFO";
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return PK_PTN.matcher(sAddress).replaceAll("PKWY");
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
}
