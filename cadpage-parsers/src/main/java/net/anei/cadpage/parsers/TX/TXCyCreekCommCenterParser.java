package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.Message;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Cy Creek Comm Center
 */
public class TXCyCreekCommCenterParser extends FieldProgramParser {
  
  private static final Pattern PART_MARKER = Pattern.compile("^\\d\\d:\\d\\d ");
  private static final Pattern DATE_PTN = Pattern.compile("(\\d+)/(\\d+)");
  private static final Pattern MARKER1 = Pattern.compile("(?:/ (?:(?:no subject|Text Message) / )?)?(?:(\\d\\d/\\d\\d) )?(?:(\\d\\d:\\d\\d) )?(?:Inc: *(\\d*);)?");
  private static final Pattern MARKER2 = Pattern.compile("(?:Assigned to Incident|Status Changed to Available|Resend Incident Information) (\\d{9}) +");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Unit: *([^ ]*) +Dispatched:.*");
  private static final Pattern MISSED_COLON_PTN = Pattern.compile("(?<=Map)(?=\\d)");
  private static final Pattern TRAILER = Pattern.compile(" +(\\d{8,}) *$");
  
  public TXCyCreekCommCenterParser() {
    super("HARRIS COUNTY", "TX",
          "Inc:ID? ADDR! Map:MAP! Sub:PLACE? Juris:SRC? Nat:CALL! Units:UNIT% X-St:X");
  }
  
  @Override
  public String getFilter() {
    return "CommCenter@ccems.com,93001,777,888,messaging@iamresponding.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected Data parseMsg(Message msg, int parseFlags) {
    
    // Sometimes the date is include in parenthesis, where it will be
    // misinterpreted as a subject or message index :(
    String body = msg.getMessageBody();
    if  (PART_MARKER.matcher(body).find()) {
      int month = -1;
      int day = -1;
      String subject = msg.getSubject();
      if (subject.length() > 0) {
        Matcher match = DATE_PTN.matcher(subject);
        if (match.matches()) {
          month = Integer.parseInt(match.group(1));
          day = Integer.parseInt(match.group(2));
        }
      } else {
        if (msg.getMsgCount() >= 0) {
          month = msg.getMsgIndex();
          day = msg.getMsgCount();
        }
      }
      if (month >= 0) {
        subject = String.format("%02d/%02d", month, day);
        body = subject + ' ' + body;
      }
      msg.setMessageBody(body);
    }
    return super.parseMsg(msg, parseFlags);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Strip message prefix
    body = stripFieldStart(body, "/ Text Message /");
    body = stripFieldStart(body, "CommCenter@ccems.com:");
    
    if (subject.equals("Text Message")) subject = "";
    data.strSource = subject;
    
    Matcher match = MARKER1.matcher(body);
    if (!match.lookingAt()) return false;  // Never happens anymore
    data.strDate = getOptGroup(match.group(1));
    data.strTime = getOptGroup(match.group(2));
    data.strCallId = getOptGroup(match.group(3));
    body = body.substring(match.end()).trim();
    
    match = TRAILER.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(0,match.start());
    }
    
    if (data.strCallId.length() == 0) {
      match = MARKER2.matcher(body);
      if (match.lookingAt()) {
        data.strCallId = match.group(1);
        body = body.substring(match.end());
      }
    }
    
    match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strUnit = match.group(1);
      data.strPlace = body;
      return true;
    }
    
    if (body.startsWith("Repage:")) body = body.substring(7).trim();
    
    body = MISSED_COLON_PTN.matcher(body).replaceAll(":");
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strCity.length() == 0 && data.strCall.contains("MA-MUTUAL AID")) data.strCity = "HOUSTON";
    
    // Misspelled street name check
    data.strAddress = fixAddress(data.strAddress);
    data.strCross = fixAddress(data.strCross);
    return true;
  }
  
  private static String fixAddress(String addr) {
    return MITTLESTED_PTN.matcher(addr).replaceAll("MITTLESTEDT");
  }
  private static final Pattern MITTLESTED_PTN = Pattern.compile("\\bMITTLESTED\\b");
  
  @Override
  public String getProgram() {
    return "DATE TIME " + super.getProgram() + " ID";
  }
    
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern VAL_PTN = Pattern.compile("\\bVAL\\b", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      field = p.get(',');
      field = VAL_PTN.matcher(field).replaceAll("VALLEY");
      parseAddressCity(field, data);
      int pt = data.strCity.indexOf(' ');
      if (pt >= 0) {
        data.strPlace = data.strCity.substring(pt+1).trim();
        data.strCity = data.strCity.substring(0,pt);
      }
      if (data.strCity.equals("HC")) data.strCity = "";
      if (data.strCity.equals("MC")) data.strCity = "MONTGOMERY COUNTY";
      if (data.strCity.equals("H") || data.strCity.equals("HO")) data.strCity = "HOUSTON";
      data.strPlace = append(data.strPlace, " - ", p.get(';'));
      data.strApt = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("/") || field.contains("&")) {
        data.strCross = field;
      } else {
        Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ONLY_CROSS | FLAG_ANCHOR_END | FLAG_IMPLIED_INTERSECT | FLAG_ALLOW_DUAL_DIRECTIONS, field);
        if (res.isValid()) {
          res.getData(data);
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_CROSS_FOLLOWS, field, data);
          data.strCross = append(data.strCross, " / ", getLeft());
        }
      }
    }
  }
}
