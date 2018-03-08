package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class NYJeffersonCountyAParser extends FieldProgramParser {
  
  public NYJeffersonCountyAParser() {
    super("JEFFERSON COUNTY", "NY",
          "( SELECT/R Incident_#:SKIP! CAD_Call_ID_#:ID! Type:SKIP! Date/Time:DATETIME! Address:ADDR1! Contact:NAME! Contact_Address:SKIP! Contact_Phone:PHONE1! Nature:CODE! Nature_Description:CALL! Determinant:SKIP! Determinant_Desc:SKIP! Complainant:SKIP! Comments:INFO! INFO/N+ " +
          "| CALL ADDR2! ( INFO2! END | X/Z? LATLON INFO2! END ) )");
  }

  private static final Pattern SUBJECT_BRK_PTN = Pattern.compile(" +(?=Incident #:|CAD Call ID #:|Type:|Date/Time:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Fixed stuff messed up by IAR edits :(
    String[] flds = null;
    if (subject.equals("So. Jeff. Rescue")) {
      data.strSource = subject;
      do {
        String work = body;
        if (work.endsWith("/") || work.endsWith(":")) work += ' ';
        work = work.replace(" : ", ":");
        
        String[] tFlds = new String[3];
        
        // Generally split by " / ".  With some special checks to see if there
        // is a legitimate slash sequence in the address or info fields
        int pt1 = work.indexOf(" / ");
        if (pt1 < 0) break;
        tFlds[0] = work.substring(0,pt1).trim();
        
        int pt2 = work.indexOf(" / ", pt1+3);
        if (pt2 < 0) break;
        
        int pt3 = work.indexOf(" / ", pt2+3);
        if (pt3 >= 0) {
          if (!work.substring(pt1+3,pt2).contains(":") &
              work.substring(pt2+3,pt3).contains(":")) {
            pt2 = pt3;
          }
        }
        tFlds[1] = work.substring(pt1+3,pt2).trim();
        tFlds[2] = work.substring(pt2+3).trim();
        flds = tFlds;
      } while (false);
    }
    
    // Normal parsing
    if (flds == null) {
      
      if (subject.startsWith("[")) {
        int pt = subject.indexOf(']');
        if (pt < 0) return false;
        data.strSource = subject.substring(1,pt).trim();
        subject = subject.substring(pt+1).trim();
      }
      
      if (subject.startsWith("DISPATCH:")) {
        data.strUnit = subject.substring(9).trim();
      }
      else if (subject.startsWith("DISPATCH") && subject.contains("Incident #:")) {
        setSelectValue("R");
        data.msgType = MsgType.RUN_REPORT;
        subject = SUBJECT_BRK_PTN.matcher(subject).replaceAll("\n");
        body = subject + "\n" + body;
        return parseFields(body.split("\n"), data);
      } 
      else if (data.strSource.length() == 0) data.strSource = subject;
      
      setSelectValue("");
      flds = body.split("\\|", -1);
    }
    return parseFields(flds, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC UNIT " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PHONE1")) return new MyPhone1Field();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("LATLON")) return new MyLatLonField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }
  
  private class MyPhone1Field extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("(   )   -")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("([A-Z]?\\d+[A-Z]?|[A-Z]|BLDG?[. ]+.*)|(?:APT|RM|ROOM|LOT) *([A-Z] \\d+[A-Z]?|\\S+) *(.*)");
  private static final Pattern ADDR_BOUND_PTN = Pattern.compile("[NSEW]B");
  
  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(";")) {
        part = part.trim();
        if (data.strAddress.length() == 0) {
          parseAddress(part, data);
          continue;
        }
        Matcher match = ADDR_APT_PTN.matcher(part);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = match.group(2).replace(" ", "");
          data.strApt = append(data.strApt, "-", apt);
          part = getOptGroup(match.group(3));
        }
        part = stripFieldStart(part, "U:");
        if (ADDR_BOUND_PTN.matcher(part).matches()) {
          data.strAddress = append(data.strAddress, " ", part);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern ADDR_PTN = Pattern.compile("\\(.\\)$");
  private class MyAddress2Field extends MyAddress1Field {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (match.find()) field = field.substring(0,match.start()).trim();
      Parser p = new Parser(field.trim());
      data.strCity = p.getLastOptional(':');
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern LAT_LON_PTN = Pattern.compile("Lat: *(\\S*) *Lon: *(\\S*)");
  private class MyLatLonField extends GPSField {
    @Override
    public boolean canFail() {
     return true; 
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = LAT_LON_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1)+','+match.group(2), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern MSPACE_PTN = Pattern.compile("  +");
  private static final Pattern CALLBACK_PTN = Pattern.compile("(?:CALLBACK=([^ ]*) +)?LAT[:=]([-+]?[\\.0-9]*) LON[:=]([-+]?[\\.0-9]*).*");
  private static final Pattern TURN_OFF_PTN = Pattern.compile("\\d{5}-\\d{3}-\\d{4} \\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d .*");
  private static final Pattern TURN_ON_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4} - .*");
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      boolean include = true;
      for (String part : field.split("\n")) {
        part = MSPACE_PTN.matcher(part.trim()).replaceAll(" ");
        Matcher match = CALLBACK_PTN.matcher(part);
        if (match.find()) {
          String phone = match.group(1);
          if (phone != null) data.strPhone = phone;
          setGPSLoc(append(match.group(2), ",", match.group(3)), data);
          include = false;
          continue;
        }
        if (include) {
          include = !TURN_OFF_PTN.matcher(part).matches();
        } else {
          include = TURN_ON_PTN.matcher(part).matches();
        }
        if (include) data.strSupp = append(data.strSupp, "\n", part);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO PHONE GPS";
    }
  }
}
