package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNBedfordCountyParser extends FieldProgramParser {
  
  private static final Pattern HIGHWAY_PTN = Pattern.compile("(HWY|RT) ?(([0-9]{1,3}) ?[A-Z]?)");
  private static final Pattern HIGHWAY_NUMBER_PTN = Pattern.compile("([0-9]){1,3}[A-Z]?");
  
  public TNBedfordCountyParser() {
    super("BEDFORD COUNTY", "TN",
           "NAME:NAME LOC:ADDR/S EVTYPE:CALL! COMMENTS:INFO");
    setBreakChar('-');
  }
  
  @Override
  public String getFilter() {
    return "BEDFORDCOUNTY911@BELLSOUTH.NET";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    String [] fields = body.split(";");
    
    return parseFields(fields, data);
  }  

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Check for Zipcode or City Name
      int cityLabel = field.indexOf("[=");
      if(cityLabel >= 0) {
        data.strCity = field.substring(cityLabel+2).trim();
        field = field.substring(0, cityLabel);
      }
      
      // Check for special place delimiter
      int place = field.indexOf("[@");
      if(place >= 0) {
        data.strPlace = field.substring(place+2);
        field = field.substring(0, place);
      }
      
      // Parse the info after the "@" sign
      int at = field.indexOf('@');
      if(at >= 0) {
        String secondSt = field.substring(at+1).trim();
        String firstSt = field.substring(0, at).trim();
        
        // First check to see if we have a street name
        if(isValidAddress(secondSt)) {
          field.replace('@', '&');
        }
        else {
          // Then, Check for highway
          Matcher hwyMatch = HIGHWAY_NUMBER_PTN.matcher(secondSt);
          if(hwyMatch.matches()) {
//            field = field.substring(0, at) + "& " + GetHwyLabel(hwyMatch.group(1)) + hwyMatch.group();
            secondSt = GetHwyLabel(hwyMatch.group(1)) + hwyMatch.group();
          }
          // Otherwise we are a place
          else {
            data.strPlace = secondSt;
            secondSt = "";
          }
        }
        
        // Replace HWY with TN or US on the first st address
        Matcher hwyMatch = HIGHWAY_PTN.matcher(firstSt);
        if(hwyMatch.find()) {
          String label = GetHwyLabel(hwyMatch.group(3));
          firstSt = hwyMatch.replaceAll(label + " " + hwyMatch.group(2));
        }
        
        // Combine first and second address together
        field = firstSt;
        if(secondSt.length() > 0) {
          field += " & " + secondSt;
        }
      }

      super.parse(field, data);
    }
    
    @Override 
    public String getFieldNames() {
      return "ADDR CITY PLACE";
    } 
    
    /***
     * Determines if a highway should have a US label or at TN label by the
     * length of the highway number passed in. 
     * @param hwyNumber - The number of the highway.
     * @return String - The label to use.
     */
    private String GetHwyLabel(String hwyNumber) {
      
      String hwyDesignation = "";
      if(hwyNumber.length() > 2) hwyDesignation = "US ";
      else hwyDesignation = "TN ";
      
      return hwyDesignation;
    }
  }
  
  // The info field can also contain cross streets
  private static final Pattern CROSS_STREET = Pattern.compile(".* : cross streets : (.*)\n?");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      
      Matcher m = CROSS_STREET.matcher(field);
      if(m.find()) {
        data.strCross = m.group(1);
        field = field.substring(m.end()).trim();
      }
      
      super.parse(field, data);
    }
    
    @Override 
    public String getFieldNames() {
      return "X INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
}
