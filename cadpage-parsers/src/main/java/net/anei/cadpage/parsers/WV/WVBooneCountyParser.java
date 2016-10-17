package net.anei.cadpage.parsers.WV;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * NOTES: In general this dispatcher uses a '-' as a field delimiter as well
 * as labels.  However, there were a couple of cases where a field is blank,
 * in which case it does not have a '-' before the next field, so I decided to 
 * process by the field names and remove excess dashes.  
 */
public class WVBooneCountyParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("\\[([0-9]-[0-9]{1,2})\\]-[- ]*(?:(.*?) -)? (Call|Nature)");
  private static final Pattern MISSING_DELIM_PTN = Pattern.compile("(?<! -) (Nature:|Comments:)");
  
  public WVBooneCountyParser() {
    super(CITY_LIST, "BOONE COUNTY", "WV",
           "Call:ID Nature:CALL! CALL+ Location:ADDR/S! Comments:INFO INFO/SDS+");
  }
  
  @Override
  public String getFilter() {
    return "paging@boonewv.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
  
    // Check to see which type of subject heading we have.
    Matcher subjectUnit = SUBJECT_PTN.matcher(subject);
    
    // If the subject heading looks like "[1-13]-- - Nature", add to body with a :
    if(subjectUnit.find()) {
      data.strUnit = subjectUnit.group(1).trim();
      data.strSupp = getOptGroup(subjectUnit.group(2));
      if (body.startsWith("donotreply:")) body = body.substring(11).trim(); 
      body = subjectUnit.group(3) + ": " + body;
    }
    // Otherwise our subject contains the unit only
    else {
      data.strUnit = subject;
      if (!body.startsWith("-- - ")) return false;
      body = body.substring(5).trim();
    }
    
    // One sample has "Loca" instead of "Location:"
    body = body.replace("Loca ", "Location: ");
    
    // Occasional missing delimiter before Comment:
    body = MISSING_DELIM_PTN.matcher(body).replaceAll(" - $1");
    
    return super.parseFields(body.split(" - "), data);
  }
  
  @Override 
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override 
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  
  private static final Pattern STREET_NUM_PTN = Pattern.compile("([0-9]{1,5}).0+\\b");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      
      // Remove the 0's found after the '.' in the address
      field = STREET_NUM_PTN.matcher(field).replaceAll("$1");
      field = field.replace("Suite:APT", "APT");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("(LAT:(?:[-+]\\d{3}.\\d{6})? LON:(?:[-+]\\d{3}.\\d{6})?)(?: COF:\\d*)?(?: COP:\\d*)? *");
  private class MyInfoField extends InfoField {
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        String gps = match.group(1);
        if (gps != null) setGPSLoc(gps, data);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city.toUpperCase(), MAP_CITIES);
  }
  
  private static final String[] CITY_LIST = {
    
    // Incorporated Cities
    "DANVILLE",
    "MADISON",
    "SYLVESTER",
    "WHITESVILLE",
    
    // Unincorporated towns and cities - Many of which Google cannot find!
    "ANDREW",
    "ASHFORD",
    "BALD KNOB",
    "BANDYTOWN",
    "BARRETT",
    "BIGSON",
    "BIG UGLY",
    "BIM",
    "BLOOMINGROSE",
    "BLUE PENNANT",
    "BOB WHITE",
    "BRADLEY",
    "BRANHAM HEIGHTS",
    "BRUSHTON",
    "BULL CREEK",
    "CAMEO",
    "CAMP CREEK",
    "CAZY",
    "CAZY BOTTOM",
    "CLINTON",
    "CLINTON CAMP",
    "CLOTHIER",
    "COMFORT",
    "COOPERTOWN",
    "COSTA",
    "COXS FORK",
    "DARTMONT",
    "DODDSON FORK",
    "DODSON JUNCTION",
    "DOG HOLLOW OF MORR",
    "DRAWDY",
    "EASLY",
    "EDEN",
    "ELK RUN JUNCTION",
    "EUNICE",
    "EMMONS",
    "FOCH",
    "FORK CREEK",
    "FOSTER",
    "FOSTER HOLLOW",
    "FOSTER HOLLOW LEFT",
    "FOSTERVILLE",
    "GARRISON",
    "GORDON",
    "GREENVIEW",
    "GREENWOOD",
    "GRIPPE",
    "HADDLETON",
    "HAVANA",
    "HEWETT",
    "HOLLY HILLS",
    "HOPKINS FORK",
    "INDIAN CREEK",
    "JANIE",
    "JEFFREY",
    "JOES CREEK",
    "JULIAN",
    "KEITH",
    "KIRBYTON",
    "KOHLSAAT",
    "LANTA",
    "LAUREL CITY",
    "LAUREL ESTATES",
    "LICK CREEK",
    "LINDYTOWN",
    "LITTLE HORSE CREEK",
    "LORY",
    "LOW GAP",
    "MANILA",
    "MARNIE",
    "MARTHATOWN",
    "MAXINE",
    "MEADOW FORK",
    "MIDDLE HORSE CREEK",
    "MIDWAY",
    "MILLTOWN",
    "MISSOURI FORK",
    "MORRISVALE",
    "MUD RIVER",
    "NELSON",
    "NELLIS",
    "NEWPORT",
    "NORTH FORK",
    "ORGAS",
    "OTTAWA",
    "PD FORK",
    "PEYTONA",
    "PONDCO",
    "POWELL CREEK",
    "PRENTER",
    "PRICE HILL",
    "PRICE HOLLOW OF AS",
    "QUINLAND",
    "RACINE",
    "RACINE HILL",
    "RAMAGE",
    "RIDGEVIEW",
    "ROBINSON",
    "ROCK CASTLE",
    "ROCK CREEK",
    "ROUNDBOTTOM OF PEY",
    "RUMBLE",
    "SAND LICK OF PRENT",
    "SECOAL",
    "SENG CREEK",
    "SETH",
    "SHARLOW",
    "SHORT CREEK",
    "SIX MILE",
    "SOUTH MADISON",
    "SPARS CREEK",
    "SPRING HOLLOW",
    "SPRUCE LAUREL",
    "STRINGTOWN",
    "TONEYS BRANCH",
    "TURTLE CREEK",
    "TWILIGHT",
    "TWIN POPLARS",
    "UNEEDA",
    "VAN",
    "WASHINGTON HEIGHTS",
    "WHARTON",
    "WILLIAMS MOUNTAIN"
  };
  
  private static final Properties MAP_CITIES = buildCodeTable(new String[]{
    "BIG UGLY",             "DANVILLE",
    "BULL CREEK",           "WHARTON",
    "CAZY BOTTOM",          "WHARTON",
    "CLINTON CAMP",         "WHARTON",
    "COXS FORK",            "DANVILLE",
    "DODDSON FORK",         "SPURLOCKVILLE",
    "DOG HOLLOW OF MORR",   "SPURLOCKVILLE",
    "DRAWDY",               "1",
    "GREENWOOD",            "WHARTON",
    "INDIAN CREEK",         "RACINE",
    "JOES CREEK",           "DANVILLE",
    "LITTLE HORSE CREEK",   "JULIAN",
    "LAUREL CITY",          "OTTAWA",
    "LAUREL ESTATES",       "BOB WHITE",
    "MANILA",               "2",
    "MAXINE",               "1",
    "MEADOW FORK",          "2",
    "MISSOURI FORK",        "HEWETT",
    "NEWPORT",              "DANVILLE",
    "PD FORK",              "FOSTER",
    "PRICE HOLLOW OF AS",   "ASHFORD",
    "QUINLAND",             "2",
    "ROBINSON",             "MADISON",
    "ROCK CASTLE",          "1",
    "ROCK CREEK",           "FOSTER",
    "ROUNDBOTTOM OF PEY",   "PEYTONA",
    "SAND LICK OF PRENT",   "SETH",
    "SPARS CREEK",          "DANVILLE",
    "SENG CREEK",           "1",
    "SHORT CREEK",          "RACINE",
    "SPRING HOLLOW",        "WHARTON",
    "STRINGTOWN",           "JEFFREY",
    "TONEYS BRANCH",        "1",
    "TWIN POPLARS",         "ORGAS"
  });
}
