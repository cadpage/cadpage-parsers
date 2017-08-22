package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class ILStClairCountyAParser extends FieldProgramParser {
  
  public ILStClairCountyAParser() {
    super("ST CLAIR COUNTY", "IL",
          "( SELECT/2 EMPTY? SRC2! Response%EMPTY! Location:ADDRCITY! Response_Type:CALL! INFO/N+? SequenceNumber:ID! Priority:PRI! INFO/N+? Agency:SRC! INFO/N+ " +
          "| Response_Type:CALL! Location:ADDRCITY! Creation_Time:DATETIME! Agency:SRC )");
  }
   
  @Override
  public String getFilter() {
    return "freeburgfire@yahoo.com,@stclaircountyil.gov,sjr5536@aol.com,svfd4801@yahoo.com";
  }

  private static final Pattern HTML_TAG_PTN = Pattern.compile("(?: *<[^>]*?> *)+");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (body.startsWith("<STYLE>")) {
      int pt = body.indexOf("</STYLE>");
      if (pt < 0) return false;
      body = body.substring(pt+8).trim();
      
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("2");
      return parseFields(HTML_TAG_PTN.split(body), data);
    }
    
    else {
      setSelectValue("1");
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  private static final String MSG_PREFIX = "Fire Run";
  private static final Pattern PTN_GARBAGE = Pattern.compile("^(\\p{ASCII}+)");
  private static final Pattern PTN_GARBAGE_PRE = Pattern.compile("(\\p{ASCII}+)$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // One of the messages has extra characters at the end.
    // Many of the others have extra characters at the beginning.  
    // Need to check both in case we need the message header.
    Matcher garbage = PTN_GARBAGE.matcher(body);
    if(garbage.find()) {
      body = garbage.group(1);
    }
    else {
      Matcher preGarbage = PTN_GARBAGE_PRE.matcher(body);
      if(preGarbage.find()) {
        body = preGarbage.group(1);
      }
    }
    
    // Remove message header for now.
    if(body.startsWith(MSG_PREFIX)) {
      body = body.substring(MSG_PREFIX.length()).trim();
    }
    
    // Put delimiter in front of Location label
    body = body.replace(" Location:", "\nLocation:");
    
    String[] fields = body.split("[\n\t]");
    return parseFields(fields, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("ADDR", "PLACE ADDR");
  }
  
  private static final String DATETIME = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}";
  @Override
  public Field getField(String name) {
    if (name.equals("SRC2")) return new SourceField("(.*?) +Closeout Report", true);
    if (name.equals("ADDRCITY")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField(DATETIME, true);
    return super.getField(name);
    
  }
  
  private static final Pattern PTN_CROSS_STREET = Pattern.compile("(.*?)\\(/?(.*)\\)(.*?)");
  private static final Pattern PTN_FULL_ADDR = Pattern.compile("(.*?, .*?)(?:, *(IL|MO))?, *\\d{5}(?: +#(.*))?");
  private static final Pattern PTN_FULL_ADDR2 = Pattern.compile("(\\d+ )+#([^ ]+) +(.*)");
  private static final Pattern PTN_INTERSECT = Pattern.compile(",[ A-Z]+/");
  private static final Pattern PTN_APT = Pattern.compile("(.*)# *([^ ]+) (.*)");
  private class MyAddressField extends AddressCityField {
    
    @Override 
    public void parse(String field, Data data) {
      
      // Add label for Cross street
      String cross = "";
      Matcher match = PTN_CROSS_STREET.matcher(field);
      if(match.matches()) {
        field = append(match.group(1).trim(), " ", match.group(3).trim());
        cross = match.group(2).trim();
        int pt = cross.indexOf(';');
        if (pt >= 0) {
          data.strPlace = cross.substring(pt+1).trim();
          cross = cross.substring(0, pt).trim();
        }
        cross = stripFieldStart(cross, "/");
        cross = stripFieldEnd(cross, "/");
      }
      
      if (field.startsWith("@")) {
        field = field.substring(1).trim();
        if (cross.length() > 0) {
          data.strPlace = field;
          field = cross;
          cross = "";
        }
      }
      data.strCross = cross;
      
      String apt = "";
      match = PTN_FULL_ADDR.matcher(field);   // This will match address, city, and zip
      if (match.matches()) {
        field = match.group(1).trim();                       // Remove the zipcode
        data.strState = getOptGroup(match.group(2));
        apt = getOptGroup(match.group(3));
      } 
      else if ((match = PTN_FULL_ADDR2.matcher(field)).matches()) {
        field = match.group(1) + match.group(3);
        apt = append(match.group(2), "-", apt);
      }
      
      // Intersections usually have cities following both street names, so get rid
      // of the first one
      field = PTN_INTERSECT.matcher(field).replaceFirst("/");
      
      // Then there are the free floating apt fields behind the street number :(
      match = PTN_APT.matcher(field);
      if (match.matches()) {
        field = append(match.group(1).trim(), " ", match.group(3).trim());
        data.strApt = append(match.group(2), "-", apt);
      }
      
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST X";
    }
  }
}
