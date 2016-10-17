package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Gaston County, NC (variant C - Specific to CodeMessaging)
 * Community
 */
public class NCGastonCountyCParser extends FieldProgramParser {
  
  private static final Pattern CODE_PTN = Pattern.compile("^\\d{1,2}[A-Z]\\d{1,2}[A-Z]?\\b");
  
  public NCGastonCountyCParser() {
    super("GASTON COUNTY", "NC",
           "CALL ADDR X CITY SRC UNIT! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseFields(body.split("\\|"), 6, data)) return false;
    if (data.strCity.equals("COUNTY")) data.strCity = ""; 
    return true;
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group();
        field = field.substring(match.end()).trim();
        if (field.length() == 0) {
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
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
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
    if (name.equals("CALL")) return new MyCallField();
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
}
