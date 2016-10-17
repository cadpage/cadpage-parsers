package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYHardinCountyCParser extends FieldProgramParser {
  
  public KYHardinCountyCParser() {
    super("HARDIN COUNTY", "KY",
          "Nature:CALL! Address:ADDR! Cross:X!+ Timeout:DATETIME! Info:INFO+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@hckysar.appspotmail.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Incident: *(\\d{4})(\\d{6})");
  
  private boolean infoActive;
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1) + '-' + match.group(2);
    body = decodeHtmlSequence(body).trim(); 
    body = body.replace('\t', ' ');
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID1")) return new MyId1Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyId1Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Report#:");
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" MP:");
      if (pt >= 0) field = field.substring(0,pt).trim();
      field = stripFieldStart(field, "*");
      field = stripFieldEnd(field, "*");
      super.parse(field,  data);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      int pt = field.indexOf(" City:");
      if (pt >= 0) {
        data.strCity = convertCodes(field.substring(pt+6).trim(), KYHardinCountyAParser.CITY_CODES);
        field = field.substring(0,pt).trim();
      }
      field = stripFieldStart(field, "*");
      field = stripFieldEnd(field, "*");
      super.parse(field, data);
      
      // See if they added a  "1" house number to what should be an intersection
      if (data.strAddress.startsWith("1 ") && data.strAddress.contains("&")) {
        data.strAddress = data.strAddress.substring(2).trim();
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d\\d)  +Time Out: *(\\d\\d?:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }
  
  private static final Pattern INFO_SKIP_PTN = Pattern.compile(" *\\[\\d\\d[\\d[A-Z]/: ]*\\]?$|^(?:\\d\\d:\\d\\d:\\d\\d )?[A-Z]*\\] *|^\\d\\d:\\d\\d:\\d\\d *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("[*Begin PowerPhone") || field.startsWith("[Begin PowerPhone")) {
        infoActive = false;
        return;
      }
      if (field.startsWith("[*End PowerPhone") || field.startsWith("[End PowerPhone")) {
        infoActive = true;
        return;
      }
      if (!infoActive) return;
      
      field = INFO_SKIP_PTN.matcher(field).replaceAll(" ").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
