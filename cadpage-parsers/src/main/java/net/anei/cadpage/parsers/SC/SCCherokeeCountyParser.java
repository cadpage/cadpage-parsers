package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class SCCherokeeCountyParser extends FieldProgramParser {
  
  public SCCherokeeCountyParser() {
    super("CHEROKEE COUNTY", "SC", 
          "Location:PLACE! Address:ADDRCITY! Address_Details:INFO! Call_Type:CALL! Pro_Qa_summary:INFO/N! External_Number:SRC_ID!");
  }
  
  @Override
  public String getFilter() {
    return "zuercher@cherokeecountysheriff.net";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("SRC_ID")) return new MySourceIdField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_ST_ZIP_PTN = Pattern.compile("(.*), ([A-Z]{2})(?: (\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_CITY_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", None");
      super.parse(field, data);
    }
  }
  
  private static final Pattern SRC_ID_PTN = Pattern.compile("([A-Za-z]+) +([A-Z]*\\d+)");
  private class MySourceIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(";")) {
        part = part.trim();
        Matcher match = SRC_ID_PTN.matcher(part);
        if (match.matches()) {
          if (data.strSource.length() == 0) data.strSource = match.group(1);
          part = match.group(2);
        }
        data.strCallId = append(data.strCallId, ",", part);
      }
    }

    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }
}
