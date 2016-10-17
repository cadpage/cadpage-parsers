package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MSNewtonCountyParser extends FieldProgramParser {
  
  public MSNewtonCountyParser() {
    super("NEWTON COUNTY", "MS",
           "CALL! Loc:ADDR! Rcvd:TIME_ID!");
  }
  
  @Override
  public String getFilter() {
    return "DO_NOT_REPLY@NEWTONCO911.COM";
  }
  
  private static final Pattern PTN_SUBJECT_RUN_REPORT = Pattern.compile("Times - ([A-Z]+)(?:\\|.*)?");
  private static final Pattern PTN_UNIT_ID = Pattern.compile("^U:([^,]+), *E:(\\d+),");
  private static final Pattern PTN_ADDRESS_BLOCK = Pattern.compile("\\bNEAR THE (\\d+) BLOCK\\b");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher subjectMatch = PTN_SUBJECT_RUN_REPORT.matcher(subject);
    if(subjectMatch.matches()) {
      // Process RUN REPORT
      data.strCall = "RUN REPORT";
      data.strSource = subjectMatch.group(1);
      
      // Parse the unit and the ID
      Matcher unitIdMatch = PTN_UNIT_ID.matcher(body);
      if(unitIdMatch.find()) {
        data.strUnit = unitIdMatch.group(1);
        data.strCallId = unitIdMatch.group(2);
        
        data.strPlace = body.substring(unitIdMatch.end()).trim();
      }
      else {
        data.strPlace = body;
      }
      
      return true;
    }
    
    return super.parseMsg(body, data);
  } 
  
  private static final Pattern PTN_CALL = Pattern.compile("(.*?) +([A-Za-z]+) \\(\\)");
  
  private class MyCallField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher m = PTN_CALL.matcher(field);
      if(!m.matches()) abort();
      data.strCall = m.group(1);
      data.strSource = m.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL SRC";
    }
    
  }
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      
      int contactIdx = field.indexOf("[@");
      if(contactIdx >= 0) {
        String name = cleanWirelessCarrier(field.substring(contactIdx+2).trim());
        if(!name.equals("UNKNOWN")) data.strName = name;
        field = field.substring(0, contactIdx).trim();
      }
      
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " NAME";
    }
  }
  
  private static final Pattern PTN_TIME_ID = Pattern.compile("(\\d{1,2}:\\d{1,2}:\\d{1,2}) (\\d+)");
  
  private class MyTimeIDField extends TimeField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher m = PTN_TIME_ID.matcher(field);
      if(!m.matches()) abort();
      data.strCallId = m.group(2);
      super.parse(m.group(1), data);
    }
    
    @Override 
    public String getFieldNames() {
      return super.getFieldNames() + " ID";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME_ID")) return new MyTimeIDField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    
    // Isolating the phrase "NEAR THE #### BLOCK" and moving the number to the beginning
    // will cause the address to be map.
    Matcher addrBlockMatch = PTN_ADDRESS_BLOCK.matcher(sAddress);
    if(addrBlockMatch.find()) {
      sAddress = addrBlockMatch.group(1) + " " + sAddress.substring(0, addrBlockMatch.start());
      sAddress = sAddress.trim();
    }
    
    // Replace "CLOSE TO" with "&" which will map better
    sAddress = sAddress.replace("CLOSE TO", "&");
    
    return super.adjustMapAddress(sAddress);
  }

}
