package net.anei.cadpage.parsers.OH;

/**
 * Washington County, OH (B)
 */

import net.anei.cadpage.parsers.FieldProgramParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWashingtonCountyBParser extends FieldProgramParser {

  public OHWashingtonCountyBParser () {
    super("WASHINGTON COUNTY", "OH",
          "( Call_Date:DATE! Call_Time:TIME! Fire_Code:CALL! Location:ADDR! Owner:PLACE! Sector:MAP! Description:INFO! " +
            "Cross_Street1:X! Cross_Street2:X! Alert1:ALERT! Alert2:ALERT/SDS! " +
          "| DATETIME2 CALL2 ADDR2 INFO2! INFO2/N X2 ) END");
  }
  
  @Override
  public String getFilter() {
    return "belprepd@gmail.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page (\\d\\d-\\d{6})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strCallId = match.group(1);
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("DATETIME2")) return new MyDateTimeField();
    if (name.equals("CALL2")) return new MyCallField();
    if (name.equals("ADDR2")) return new MyAddressField();
    if (name.equals("INFO2")) return new MyInfoField();
    if (name.equals("X2")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) @ (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Report of -- ");
      field = stripFieldEnd(field, " at");
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*)\\(Sector=(\\d+)\\)");
  private static final Pattern ADDRESS_PTN1 = Pattern.compile("At (.*?),(.*?)\\((.*)\\)");
  private static final Pattern ADDRESS_PTN2 = Pattern.compile("(.*?) / *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strMap = match.group(2);
      }
      match = ADDRESS_PTN1.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim().replace('@', '&'), data);
        data.strCity = match.group(2).trim();
        data.strPlace = match.group(3).trim();
        return;
      }
      
      match = ADDRESS_PTN2.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim().replace('@', '&'), data);
        data.strPlace = match.group(2).trim();
        return;
      }
      abort();
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE MAP";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Call Description is=> ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern CROSS_PTN = Pattern.compile("Cross Streets are - (.*?) and\\b(.*)");
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CROSS_PTN.matcher(field);
      if (match.matches()) {
        data.strCross = append(match.group(1).trim(), " / ", match.group(2).trim());
      } else {
        if (!field.startsWith("Cross Streets")) abort();
        data.strCross = field.substring(13).trim();
      }
    }
  }
}