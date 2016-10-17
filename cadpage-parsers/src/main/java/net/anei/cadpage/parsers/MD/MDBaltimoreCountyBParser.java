package net.anei.cadpage.parsers.MD;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Baltimore County, MD
 */
public class MDBaltimoreCountyBParser extends FieldProgramParser {
  
  public MDBaltimoreCountyBParser() {
    super("BALTIMORE COUNTY", "MD",
           "BOX:BOX CALL:CALL! ADDR:ADDR! PL:PLACE UNIT:UNIT! INFO:INFO DATE:DATE TIME:TIME ID:ID%");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('', '\'');
    if (!parseFields(body.split("\n"), data)) return false;
    
    // Address and place names get crossed when we deal with interchanges or mile markers
    if (data.strPlace.contains(" MM ") || data.strPlace.contains(" BET ")) {
      data.strCross = data.strAddress.replace('*', '/');
      data.strAddress = data.strPlace;
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("PLACE", "PLACE X");
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("[A-Z0-9]{2,3}-\\d{2,3}|MUTAID", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern UPDATED_LOC = Pattern.compile("[ \\*/]*\\bUPDATED LOC\\b[ \\*/]*");
  private static final Pattern DIR_BOUND = Pattern.compile("\\b([NSEW])/B\\b");
  private static final Pattern APT_MARKER = Pattern.compile(" +(APT|ROOM|RM|BLDG|SUITE|CONDO) +");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      field = UPDATED_LOC.matcher(field).replaceAll("");
      field = DIR_BOUND.matcher(field).replaceAll("$1B");
      field = field.replace('@', '&');
      
      String apt = "";
      Matcher match = APT_MARKER.matcher(field);
      if (match.find()) {
        apt = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
        String type = match.group(1);
        if (type.equals("BLDG")) apt = append(type, " ", apt);
      }
      
      super.parse(field, data);
      data.strApt = append(data.strApt, " ", apt);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      // Break field up into blank delimited tokens
      List<String> unitList = new ArrayList<String>();
      for (String part : field.split(" +")) {
        
        // Skip single digits
        if (part.length() == 1 && Character.isDigit(part.charAt(0))) continue;
        
        // Skip duplicate box number
        if (part.equals(data.strBox)) continue;
        
        // If it starts with FS add to source field
        if (part.startsWith("FS")) {
          data.strSource = append(data.strSource, " ", part);
          continue;
        }
        
        // Add to unit list unless it is already in there somewhere
        if (!unitList.contains(part)) unitList.add(part);
      }
      
      // Finally unpack list into unit field
      StringBuilder sb = new StringBuilder();
      for (String part : unitList) {
        if (sb.length() > 0) sb.append(' ');
        sb.append(part);
      }
      data.strUnit = sb.toString();
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT SRC";
    }
  }
  
  private class MyDateField extends DateField {
    
    public MyDateField() {
      super("\\d\\d-\\d\\d-\\d\\d", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace('-', '/');
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = GR_PTN.matcher(addr).replaceAll("GARTH");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern GR_PTN = Pattern.compile("\\bGR\\b");
}
