package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PACentreCountyBParser extends FieldProgramParser {
  
  public PACentreCountyBParser() {
    super("CENTRE COUNTY", "PA", 
          "( SELECT/1 Box:BOX_CALL_ADDR! Due:UNIT? Name:NAME " + 
          "| Box:BOX! ( CALL_ADDR/Z! END | CALL_ADDR EXTRA END | CALL PLACE? ADDRCITY! EMPTY+? MAP Name:NAME Due:UNIT END ) )");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("TWP");
  }
  
  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com";
  }
  
  private static final Pattern HTTP_PTN = Pattern.compile("[ \n]http:");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Centre County Alerts")) return false;
    Matcher match = HTTP_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    body = stripFieldEnd(body, ".");
    if (body.contains("\n")) {
      setSelectValue("2");
      return parseFields(body.split("\n"), data);
    }
    
    else {
      setSelectValue("1");
      return super.parseMsg(body, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("BOX_CALL_ADDR")) return new MyBoxCallAddressField();
    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern BOX_PTN = Pattern.compile("(\\d{3,}) +");
  private class MyBoxCallAddressField extends MyCallAddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = BOX_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strBox = match.group(1);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "BOX " + super.getFieldNames();
    }
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*)([NSEW][NSEW]? SECTOR)");
  private static final Pattern CALL_ADDR_PTN1 = Pattern.compile("(.*- ?[AB]LS(?: Urgent)?) *(.*)");
  private static final Pattern CALL_ADDR_PTN2 = Pattern.compile("(.*?) ([^a-z,]*)");
  private static final Pattern STATION_PTN = Pattern.compile("(STATION \\d{1,2}) +(.*)");
  private class MyCallAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {

      field = MBLANK_PTN.matcher(field).replaceAll(" ");

      String call = CALL_LIST.getCode(field);
      if (call != null) {
        field = field.substring(call.length()).trim();
      } else {
        Matcher match = CALL_ADDR_PTN1.matcher(field);
        if (!match.matches()) {
          match = CALL_ADDR_PTN2.matcher(field);
          if (!match.matches()) return false;
        }
        call = match.group(1).trim();
        field = match.group(2).trim();
      }
      if (field.length() == 0) return false;
      
      data.strCall = call;
      
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strMap = match.group(2);
      }
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      String prefix = "";
      match = STATION_PTN.matcher(field);
      if (match.lookingAt()) {
        prefix = match.group(1);
        field = match.group(2);
      }
      parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, field.replace('@',  '&'), data);
      data.strPlace = append(prefix, " ", data.strPlace);
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CALL PLACE ADDR APT CITY MAP";
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(.*)(?:LOT|APT|RM|ROOM) *(.*)");
  private class MyExtraField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strMap = match.group(2).trim();
      }
      
      match = APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strApt = append(data.strApt, "-", match.group(2));
      }
      data.strPlace = field;
    }

    @Override
    public String getFieldNames() {
      // TODO Auto-generated method stub
      return null;
    }
    
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BALD EAGLE",
    "BELL HOLLOW",
    "BIG OAK",
    "BRUSH VALLEY",
    "BUFFALO RUN",
    "BULLIT RUN",
    "BUSH HOLLOW",
    "CENTENNIAL HILLS",
    "CURTIN NARROWS",
    "DIX RUN",
    "EAGLE VALLEY",
    "EGYPT HOLLOW",
    "END OF LINE",
    "FLAT ROCK",
    "FOWLER HOLLOW",
    "GOVERNORS PARK",
    "HALFMOON VALLEY",
    "HECKMAN CEMETERY",
    "JACK STRAW",
    "JAMES HILL",
    "LIONS HILL",
    "LITTLE MARSH CREEK",
    "MATCH FACTORY",
    "MEDICAL PARK",
    "MOOSE RUN",
    "PENNS VALLEY",
    "PHILIPSBURG BIGLER",
    "PINE CREEK",
    "PORT MATILDA",
    "PURDUE MOUNTAIN",
    "RABBIT HILL",
    "REESE HOLLOW",
    "RISHEL HILL",
    "ROLLING RIDGE",
    "ROSS HILL",
    "SKYTOP MOUNTAIN",
    "SNO FOUNTAIN",
    "SPORTS CAMP",
    "STEELE HOLLOW",
    "SUMMIT HILL",
    "SUNNYSIDE HOLLOW",
    "SWAMP POODLE",
    "TOW HILL",
    "TURKEY EYE",
    "VALLEY VIEW",
    "VIRGINIA PINE",
    "WEAVER HILL",
    "WHITE PINE"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "Abdom/Back Pain-ALS",
      "Allergic Reaction-ALS",
      "Automatic Fire Alarm",
      "Bleeding (Non-Trauma)-ALS",
      "Bleeding (Non-Trauma)-BLS",
      "Breathing Difficulty-ALS",
      "Building Fire",
      "BURNS Therm/Elec/Chem-ALS",
      "Cardiac/Resp Arrest ADULT-ALS",
      "Chest Pain/Heart Problems - ALS",
      "Chimney Fire",
      "<<Choose Call Type>>",
      "CO Alarm Activation",
      "Diabetic-ALS",
      "Dwelling Fire",
      "Falls/Accidents-ALS",
      "Falls/Accidents-BLS",
      "Falls/Accidents-BLS Urgent",
      "Fire Police",
      "Hazardous Conditions",
      "Investigation Inside",
      "Landing Zone",
      "Medical Assist - Emergency",
      "Medical Assist - Non-Emergency",
      "Mental/Emotional/Psych-BLS",
      ">New Call<",
      "Nuisance Fire",
      "Overdose/Poisoning-ALS",
      "Public Service",
      "Rescue-Collapse",
      "Rescue-Ground",
      "Routine TransportMNMC",
      "Seizures-ALS",
      "Sick/Unknown-ALS",
      "Sick/Unknown-BLS",
      "Stand-By",
      "Stroke/CVA-ALS",
      "TEST",
      "Transfer Assignment",
      "Uncon/Unresp/Syncope-ALS",
      "Unknown Fire Outside",
      "Vehicle Crash No Injuries No Haz",
      "Vehicle Crash Rollover No Inj",
      "Vehicle Crash Rollover W/ Inj",
      "Vehicle Crash Unk Inj",
      "Vehicle Crash Unkown Inj",
      "Vehicle Crash W/ Entrapment",
      "Vehicle Crash W/ Entrapment178",
      "Vehicle Crash W/ Hazards No Injuries",
      "Vehicle Crash W/ Inj",
      "Vehicle Fire - Small",
      "Wild Fire",
      "Wire Down"
  );
}
