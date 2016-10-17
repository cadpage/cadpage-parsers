package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class OHGreeneCountyParser extends FieldProgramParser {
  
  private static final Pattern MISSED_BLANK_PTN = Pattern.compile("([^ ])(Info:)");
  
  public OHGreeneCountyParser() {
    super(CITY_LIST, "GREENE COUNTY", "OH",
           "CALL Location:ADDR/SXXx! Time:TIME Units:UNIT Common_Name:PLACE Info:INFO ( Problem:CALL Patient_Info:INFO | Nature_Of_Call:CALL ) Incident_#:ID Narrative:INFO");
  }
  
  @Override
  public String getFilter() {
    return "psisn_dispatch@ci.xenia.oh.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = MISSED_BLANK_PTN.matcher(body).replaceAll("$1 $2");
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new MyIdInfoField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(".")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern MM_PTN = Pattern.compile("\\d+MM");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {

      super.parse(field, data);
      if (MM_PTN.matcher(data.strApt).matches()) {
        data.strAddress = append(data.strAddress, " ", data.strApt);
        data.strApt = "";
      }
      if (data.strCross.equalsIgnoreCase("No Cross Streets Found")) data.strCross = "";
      if (data.strCity.equals("CAESARCREEK TWP")) data.strCity = "CAESARSCREEK TWP";
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE X CITY";
    }
  }

  private static Pattern ID_INFO_PTN = Pattern.compile("(\\d+-\\d+) *(.*)");
  private class MyIdInfoField extends MyInfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_INFO_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1);
        field = match.group(2).trim();
      }
      DispatchProQAParser.parseProQAData(false, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ID " + super.getFieldNames();
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      DispatchProQAParser.parseProQAData(false, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO CODE";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "BEAVERCREEK",
    "BELLBROOK",
    "CENTERVILLE",
    "FAIRBORN",
    "HUBER HEIGHTS",
    "KETTERING",
    "XENIA",
    
    // Villages
    "BOWERSVILLE",
    "CEDARVILLE",
    "CLIFTON",
    "JAMESTOWN",
    "SPRING VALLEY",
    "YELLOW SPRINGS",
    
    // Townships
    "BATH TWP",
    "BEAVERCREEK TWP",
    "CAESARCREEK TWP",
    "CEDARVILLE TWP",
    "JEFFERSON TWP",
    "MIAMI TWP",
    "NEW JASPER TWP",
    "ROSS TWP",
    "SILVERCREEK TWP",
    "SPRING VALLEY TWP",
    "SUGARCREEK TWP",
    "XENIA TWP",

    //Other
    "SHAWNEE HILLS",
    "WILBERFORCE",
    "WRIGHT-PATTERSON AIR FORCE BASE",
    "BYRON",
    "OLDTOWN",
    
    "CLARK COUNTY",
    "CLINTON COUNTY",
    "FAYETTE COUNTY",
    "MADISON COUNTY",
    "MIAMI COUNTY",
    "MONTGOMERY COUNTY",
    "WARREN COUNTY"

  };
}
