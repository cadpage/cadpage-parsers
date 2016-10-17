package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCPittCountyParser extends FieldProgramParser {
  
  private static final Pattern CONFIRM_FIRE_MARKER = Pattern.compile(">>ADDRESS: +(?:0 +-|(.*?)) +>>NOTES: *(.*?)(?: // (.*))?");
  private static final Pattern DIR_B_PTN = Pattern.compile("\\b(NORTH|SOUTH|EAST|WEST) +(B)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("^(?:APT|RM|ROOM) *", Pattern.CASE_INSENSITIVE);
  
  public NCPittCountyParser() {
    super("PITT COUNTY", "NC",
           "SRC! Rcvd:DATETIME! Rsn:CALL! CALL/SDS+ Note:INFO! INFO/SLLS+ Adr:ADDR");
    addExtendedDirections();
    setupMultiWordStreets("MARTIN LUTHER KING JR", "STOKETOWN ST JOHN");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@pittcountync.gov";
  }
  
  private static final Pattern ADDR_PFX_PTN = Pattern.compile("(AREA OF) +(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("911 Fire Call")) return false;
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace('\n', ' ');

    Matcher match = DIR_B_PTN.matcher(body);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        match.appendReplacement(sb, match.group(1).substring(0,1)+match.group(2));
      } while (match.find());
      match.appendTail(sb);
      body = sb.toString();
    }
    
    match = CONFIRM_FIRE_MARKER.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT INFO");
      data.strCall = "WORKING INCIDENT";
      String addr = match.group(1);
      String apt = null;
      if (addr == null) {
        addr = match.group(2);
      } else {
        apt = match.group(2).trim();
      }
      parseAddress(addr.trim(), data);
      if (apt != null) {
        Matcher match2 = APT_PTN.matcher(apt);
        if (match2.find()) apt = apt.substring(match2.end());
        data.strApt = append(data.strApt, "-", apt);
      }
      
      data.strSupp = getOptGroup(match.group(3));
      return true;
    }
    
    if (!parseFields(body.split("\\|"), data)) return false;
    
    if (data.strAddress.length() == 0) {
      int bestNdx = -1;
      String bestPrefix = "";
      String bestCross = "";
      Result bestResult = null;
      String[] parts = data.strSupp.split("//");
      for (int ii = 0; ii<parts.length; ii++) {
        String part = parts[ii];
        parts[ii] = part.trim();
        
        part = stripFieldStart(part, "ON ");
        
        String pfx = "";
        match = ADDR_PFX_PTN.matcher(part);
        if (match.matches()) {
          pfx = match.group(1);
          part = match.group(2).trim();
        }
        String cross = "";
        pt = part.indexOf(" BETWEEN ");
        if (pt >= 0) {
          cross = part.substring(pt+9).trim();
          part = part.substring(0,pt).trim();
        }
        Result result = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS, part);
        if (bestResult == null || result.getStatus() > bestResult.getStatus()) {
          bestPrefix = pfx;
          bestCross = cross;
          bestResult = result;
          bestNdx = ii;
        }
      }
      for (int ii = 0; ii<bestNdx; ii++) {
        data.strPlace = append(data.strPlace, " - ", parts[ii]);
      }
      bestResult.getData(data);
      data.strSupp = bestResult.getLeft();
      data.strAddress = append(bestPrefix, " ", data.strAddress);
      data.strCross = append(bestCross, " / ", data.strCross);
      for (int ii = bestNdx+1; ii<parts.length; ii++) {
        String part = parts[ii];
        if (ii == bestNdx+1 && bestResult.getStatus() == STATUS_STREET_NAME && checkAddress(part) == STATUS_STREET_NAME) {
          data.strAddress = append(data.strAddress, " & ", part);
        } else {
          data.strSupp = append(data.strSupp, " // ", part);
        }
      }
    } else if (data.strSupp.equals("SAME")) {
      data.strSupp = "";
    }
    
    if (data.strPlace.startsWith("NEAR ")) {
      String place = data.strPlace.substring(5).trim();
      if (isValidAddress(place)) {
        data.strAddress = append(data.strAddress, " ", data.strPlace);
        data.strPlace = "";
      }
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " PLACE X ADDR";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("SAME")) return new SkipField("SAME", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strCross = field.substring(0,pt).trim();
        data.strPlace = field.substring(pt+1).trim();
      } else {
        if (field.startsWith("NEAR ")) {
          data.strPlace = field;
        } if (isValidAddress(field)) {
          data.strCross = field;
        } else {
          data.strPlace = field;
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("0") || field.startsWith("0 ")) field = "";
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldStart(addr, "AREA OF ");
    return super.adjustMapAddress(addr);
  }
}
