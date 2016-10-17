package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Gaston County, NC
 */
public class NCGastonCountyAParser extends FieldProgramParser {
  
  private static final Pattern TRAILER = Pattern.compile("\\b(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) \\d{1,2}\\b");
  private static final Pattern TRAILER2 = Pattern.compile("Station (\\d+)$");
  private static final String TRAILER3 = "NN/NN/NNNN NN;NN;NN NN";
  private static final Pattern CODE_PTN = Pattern.compile("^\\d{1,2}[A-Z]\\d{1,2}[A-Z]?\\b");
  
  public NCGastonCountyAParser() {
    super(CITY_LIST, "GASTON COUNTY", "NC",
           "ADDR/SC! X-ST:X! Phone:PHONE? Station:SRC Quadrant:MAP");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    
    Matcher match = TRAILER.matcher(body);
    if (match.find()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strUnit = body.substring(match.end()).trim();
      body = body.substring(0,match.start()).trim();
    } else {
      int pt1 = body.lastIndexOf(' ');
      if (pt1 <= 0) return false;
      int pt2 = body.lastIndexOf(' ', pt1-1);
      if (pt2 <= 0) return false;
      int pt3 = body.lastIndexOf(' ', pt2-1);
      if (pt3 < 0) return false;
      boolean found = false;
      for (int pt :  new int[]{pt3, pt2, pt1}) {
        String tail = body.substring(pt+1);
        String tail2 = tail.replaceAll("\\d", "N");
        if (TRAILER3.startsWith(tail2)) {
          body = body.substring(0,pt).trim();
          int len = tail.length();
          if (len >= 19) len = 19;
          else if (len >= 16) len = 16;
          else len = 0;
          if (len > 0) {
            data.strDate = tail.substring(0,10);
            data.strTime = tail.substring(11,len);
          }
          found = true;
          data.expectMore = true;
          break;
        }
      }
      if (!found) return false;
    }
    
    String defSource = "";
    match = TRAILER2.matcher(body);
    if (match.find()) {
      defSource = match.group(1);
      body = body.substring(0,match.start()).trim();
    }
      
    body = body.replace('~', ' ');
    if (!super.parseMsg(body, data)) return false;
    
    match = CODE_PTN.matcher(data.strCall);
    if (match.find()) {
      data.strCode = match.group();
      data.strCall = data.strCall.substring(match.end()).trim();
      if (data.strCall.length() == 0) {
        String code = data.strCode;
        String call = CALL_CODES.getProperty(code);
        if (call == null) {
          int pt = 0;
          while (pt < code.length() && Character.isDigit(code.charAt(pt))) pt++;
          if (++pt < code.length()) {
            code = code.substring(0,pt);
            call = CALL_CODES.getProperty(code);
          }
        }
        if (call == null) call = "EMS ALERT";
        data.strCall = call;
      }
    }
    if (data.strSource.length() == 0) data.strSource = defSource;
    return true;
  }
  
  @Override
  public String getProgram() {
    return "CODE " + super.getProgram() + " DATE TIME UNIT";
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      super.parse(stripDash(field), data);
    }
  }
  
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      super.parse(stripDash(field), data);
    }
  }
  
  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = stripDash(field);
      if (field.startsWith("Station ")) field = field.substring(8).trim();
      super.parse(field, data);
    }
  }
  
  private static String stripDash(String field) {
    if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
    return field;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "1D",      "Abdominal Pain Not alert",
      "2D",      "Allergic reactions/Hives/Medical reactions",
      "3D",      "Animal Bite",
      "4D",      "Assault / Sexual Assault",
      "5D",      "Back Pain (Non-Traumatic or Recent)",
      "6D",      "Breathing Problems",
      "7C",      "Burns / Explosion",
      "7D",      "Burns / Explosion Multi Victims",
      "8D",      "Carbon Monoxide/Inhalation/Hazmat",
      "9B1G",    "Cardiac Arrest / Death Submersion",
      "9E",      "Cardiac Arrest / Death",
      "10D",     "Chest Pain",
      "11D",     "Choking",
      "12D",     "Convulsions / Seizures",
      "13D",     "Diabetic Problems",
      "14D",     "Drowning (Near) / Diving Accident",
      "15D",     "Electrocution / Lightning",
      "16D",     "Eye Problems / Injuries",
      "17D",     "Falls / Back Injuries (Traumatic)",
      "19D",     "Heart Problems",
      "20D2H",   "Heat Exposure",
      "20D2C",   "Cold Exposure",
      "21D",     "Hemorrhage / Lacerations",
      "22D",     "Trench Collapse",
      "23D",     "Overdose / Poisoning (Ingestion)",
      "24D",     "Pregnancy / Childbirth/ Miscarriage",
      "25D",     "Psychiatric / Suicide Attempt",
      "26D",     "Sick Person (Specific Diagnosis)",
      "27D",     "Stab / Gunshot / Penetrating Trauma",
      "28C",     "Stroke (CVA)",
      "29B",     "MVA possible injuries",
      "29D",     "MVA with rollover, entrapment, ejection, etc..",
      "30D",     "Traumatic Injury (Specific)",
      "31D",     "Unconscious / Fainting (Near)",
      "32D",     "Unknown Problem (Man Down)"
  });
  
  private static final String[] CITY_LIST = new  String[]{
    
    // Cities
    "BELMONT",
    "BESSEMER CITY",
    "CHERRYVILLE",
    "GASTONIA",
    "KINGS MOUNTAIN",
    "LOWELL",
    "MOUNT HOLLY",

    // Towns
    "CRAMERTON",
    "DALLAS",
    "DELLVIEW",
    "HIGH SHOALS",
    "MCADENVILLE",
    "RANLO",
    "SPENCER MOUNTAIN",
    "STANLEY",

    // Townships
    "CHERRYVILLE",
    "CROWDERS MOUNTAIN",
    "DALLAS",
    "GASTONIA",
    "RIVERBEND",
    "SOUTH POINT",

    // Unincorporated communities
    "ALEXIS",
    "ASHEBROOK PARK",
    "BOOGERTOWN",
    "BROWN TOWN",
    "CROWDERS",
    "HARDINS",
    "LUCIA",
    "MOUNTAIN ISLAND",
    "SOUTH GASTONIA",
    "SPRINGDALE",
    "TRYON"
  };
}
