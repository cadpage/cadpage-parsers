package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KSJacksonCountyBParser extends FieldProgramParser {
  
  private Field auxAddressCityStateField;
  
  public KSJacksonCountyBParser() {
    super(CITY_LIST, "JACKSON COUNTY", "KS", 
          "Run_Number:ID! Priority:PRI! Referring_Hospital:ADDRCITYST/S! PHONE Location_of_Patient:APT! APT/S+ Receiving_Hospital:LINFO! INFO/N+");
    auxAddressCityStateField = new AuxAddressCityStateField();
  }
  
  @Override
  public String getFilter() {
    return "techsems1@gmail.com";
  }
  
  private static final Pattern FIX_BRK_PTN = Pattern.compile("\n(City, KS\\. \\d{5} )");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    data.strUnit = subject.replace(" ", "");
    Matcher match = FIX_BRK_PTN.matcher(body);
    if (match.find()) body = body.substring(0, match.start()) + ' ' + match.group(1) + '\n' + body.substring(match.end());
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.isEmpty()) {
      String addr = data.strApt;
      data.strApt = "";
      auxAddressCityStateField.parse(addr, data);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern  CITY_DELIM_PTN = Pattern.compile("\\.(?=[A-Za-z ]+,| +KS\\b)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("See Notes For Facility/Residence") ||
          field.equals("Other Facility/Residence see notes") ||
          field.equals("Other Facility or Residence")) return;
      
      int pt = field.indexOf("- ");
      if (pt < 0) {
        pt = field.lastIndexOf('-');
        if (pt < 0) abort();
      }
      data.strPlace = field.substring(0,pt).trim();
      field = field.substring(pt+2).trim();
      field = CITY_DELIM_PTN.matcher(field).replaceFirst(",").replace(".", "");
      super.parse(field, data);
    }
  }
  
  private static final Pattern TRAIL_APT_PTN1 = Pattern.compile("(.*) - ([^,]+)");
  private static final Pattern TRAIL_APT_PTN2 = Pattern.compile("(.*) ((?:RM|ROOM|ER|APT) [^,]+)");
  private class AuxAddressCityStateField extends AddressCityStateField {
    
    public AuxAddressCityStateField() {
      setQual("S");
    }
    
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = TRAIL_APT_PTN1.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(2);
      } else {
        match = TRAIL_APT_PTN2.matcher(field);
        if (match.matches()) {
          field = match.group(1);
          apt = match.group(2);
        }
      }
      
      int pt = field.lastIndexOf(" - ");
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+3).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
      
      if (data.strPlace.isEmpty()) {
        field = data.strAddress;
        data.strAddress = "";
        KSJacksonCountyBParser.this.parseAddress(StartType.START_PLACE, FLAG_NO_CITY, field, data);
        if (data.strAddress.isEmpty()) {
          data.strAddress = data.strPlace;
          data.strPlace = "";
        }
      }
    }
  }
  
  private static final Pattern LEAD_ZIP_PTN = Pattern.compile("\\d{5} +(.*)");
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = LEAD_ZIP_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      int pt = field.indexOf('{');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (isLastField() && field.equals("Notes:")) return;
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[] {
      
      // Cities
      "CIRCLEVILLE",
      "DELIA",
      "DENISON",
      "HOLTON",
      "HOYT",
      "MAYETTA",
      "NETAWAKA",
      "SOLDIER",
      "WHITING",

      // Unincorporated communities
      "BIRMINGHAM",
      "LARKINBURG",

      // Indian reservations
      "KICKAPOO",
      "PRAIRIE BAND POTAWATOMI",
      
      // Atchison County
      "ATCHISON",
      
      // Clay county, MO
      "LIBERTY",
      
      // Douglas County
      "LAWRENCE",
      
      // Franklin County
      "OTTAWA",
      
      // Geary County
      "ALBANY",
      "JUNCTION CITY",
      
      // Johnson County
      "OVERLAND PARK",
      
      // Leavenworth County
      "LANSING",
      "LEAVENWORTH",
      
      // Lynn County
      "EMPORIA",
      
      // Pottawatomie County,
      "ONAGA",
      "WAMEGO",
      
      // Riley County
      "FT RILEY",
      "MANHATTAN",
      
      // Saline County
      "SALINA",
      
      // Sedgewick County
      "WICHITA",
      
      // Shawnee County
      "TOPEKA",
      
      // Wyandotte County
      "KANSAS CITY"
  };
}
