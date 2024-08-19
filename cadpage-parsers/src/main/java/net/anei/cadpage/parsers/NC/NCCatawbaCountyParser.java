package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCCatawbaCountyParser extends DispatchA3Parser {

  private static final Pattern C_AND_B = Pattern.compile("\\bC AND B\\b|\\bC *& *B\\b", Pattern.CASE_INSENSITIVE);

  public NCCatawbaCountyParser() {
    super("CATAWBA COUNTY", "NC",
          "ID? ADDR APT CH CITY ( X2 CALL UNIT! | X/Z CALL UNIT! | X X MAP INFO1 SKIP CALL! PLACENAME PHONE UNIT ) INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (body.length() == 0) return false;
    body = body.replaceAll("//+", "/");
    body = C_AND_B.matcher(body).replaceAll("C%B");
    if (!super.parseFields(body.split("\\*"), data)) return false;
    data.strAddress = data.strAddress.replace("C%B", "C AND B");
    data.strCross = data.strCross.replace("C%B", "C AND B");

    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X2")) return new MyCross2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCross2Field extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;
      parse(field, data);
      return true;
    }
  }

  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!checkCall(field)) return false;
      parse(field, data);
      return true;
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return C_AND_B.matcher(addr).replaceAll("C%B");
  }

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return sAddress.replace("C%B", "C AND B");
  }

  @Override
  public boolean checkCall(String call) {
    return CALL_LIST.contains(call);
  }

  private class MyInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Primary Response District -")) {
        data.strSource = field.substring(27).trim();
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " SRC";
    }
  }

  private static final Set<String> CALL_LIST = new HashSet<>(Arrays.asList(
      "ABDOMINAL",
      "ALLERGIES",
      "ANIMAL RESCUE",
      "ASSAULT/RAPE",
      "BOAT ACCIDENT",
      "BREATHING",
      "CAD TESTING COMPLAINT TYPE",
      "CARBON MONOXIDE DETECTOR",
      "CARDIAC/RESPIRATORY",
      "CHEST PAIN",
      "CHIMNEY FIRE",
      "City Ordinance Violation",
      "CONTROL BURN",
      "CONVULSIONS / SEIZURES",
      "DIABETIC PROBLEMS",
      "DUMPSTER FIRE",
      "ELEVATOR RESCUE",
      "EMS ASSIST",
      "EXPLOSION",
      "FALLS",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENCE",
      "FIRE ALARM RESIDENTIAL",
      "FUEL SPILL",
      "GAS ALARM",
      "GAS LEAK/SMELL",
      "GRASS/WOODS FIRE",
      "HAZMAT SPILL",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEMORRHAGE/LACERATION",
      "ILLEGAL BURN",
      "LANDING ZONE",
      "MUTUAL AIDE ASSIST",
      "NEONATAL TRANSFER",
      "ODOR INVEST",
      "Overdose (L)",
      "OVERDOSE/POISONING",
      "POWERLINE DOWN",
      "PREGNANCY/CHILDBIRTH",
      "PSYCHIATRIC/SUICIDE",
      "PUBLIC ASSIST",
      "PUBLIC RELATIONS",
      "RESCUE STANDBY",
      "SERVICE CALL",
      "SICK PERSON",
      "SMOKE REPORT/SCARE",
      "STANDBY/MOVE UP",
      "STROKE",
      "STRUCTURE FIRE COMMERCIAL",
      "STRUCTURE FIRE RESIDENTIAL",
      "TESTING ALARM",
      "TRAFFIC ACC",
      "TRAFFIC ACC (INJURY)",
      "TRAFFIC ACC (PIN/ENT/OT)",
      "TRAFFIC ACC (STRUCTURE)",
      "TRAINING",
      "TRANSFER/EMERGENCY",
      "TRANSFER/ROUTINE",
      "TRAUMATIC INJURIES",
      "TREE DOWN",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "VEHICLE FIRE COMMERCIAL",
      "VEHICLE FIRE PASSENGER",
      "WATER RESCUE",
      "yABDOMINAL ALPHA",
      "yABDOMINAL BRAVO",
      "yABDOMINAL CHARLIE",
      "yABDOMINAL DELTA",
      "yABDOMINAL ECHO",
      "yALLERGIES ALPHA",
      "yALLERGIES BRAVO",
      "yALLERGIES CHARLIE",
      "yALLERGIES DELTA",
      "yALLERGIES ECHO",
      "yANIMAL BITES ALPHA",
      "yANIMAL BITES BRAVO",
      "yANIMAL BITES CHARLIE",
      "yANIMAL BITES DELTA",
      "yANIMAL BITES ECHO",
      "yASSAULT/ RAPE ALPHA",
      "yASSAULT/ RAPE BRAVO",
      "yASSAULT/ RAPE CHARLIE",
      "yASSAULT/ RAPE DELTA",
      "yASSAULT/ RAPE ECHO",
      "yBACK PAIN CHARLIE",
      "yBREATHING CHARLIE",
      "yBREATHING DELTA",
      "yBURNS/EXPLOSION ALPHA",
      "yBURNS/EXPLOSION BRAVO",
      "yBURNS/EXPLOSION CHARLIE",
      "yBURNS/EXPLOSION DELTA",
      "yBURNS/EXPLOSION ECHO",
      "yCARDIAC/RESPIRATORY ALPHA",
      "yCARDIAC/RESPIRATORY BRAVO",
      "yCARDIAC/RESPIRATORY CHARLIE",
      "yCARDIAC/RESPIRATORY DELTA",
      "yCARDIAC/RESPIRATORY ECHO",
      "yCHOKING ALPHA",
      "yCHOKING BRAVO",
      "yCHOKING CHARLIE",
      "yCHOKING DELTA",
      "yCHOKING ECHO",
      "yCONVULSIONS/SEIZURES ALPHA",
      "yCONVULSIONS/SEIZURES BRAVO",
      "yCONVULSIONS/SEIZURES CHARLIE",
      "yCONVULSIONS/SEIZURES DELTA",
      "yCONVULSIONS/SEIZURES ECHO",
      "yCHEST PAIN ALPHA",
      "yCHEST PAIN BRAVO",
      "yCHEST PAIN CHARLIE",
      "yCHEST PAIN DELTA",
      "yCHEST PAIN ECHO",
      "yDIABETIC PROBLEMS ALPHA",
      "yDIABETIC PROBLEMS BRAVO",
      "yDIABETIC PROBLEMS CHARLIE",
      "yDIABETIC PROBLEMS DELTA",
      "yDIABETIC PROBLEMS ECHO",
      "yFALLS ALPHA",
      "yFALLS BRAVO",
      "yFALLS CHARLIE",
      "yFALLS DELTA",
      "yFALLS ECHO",
      "yHEADACHE ALPHA",
      "yHEADACHE BRAVE",
      "yHEADACHE CHARLIE",
      "yHEADACHE DELTA",
      "yHEADACHE ECHO",
      "yHEART PROBLEMS ALPHA",
      "yHEART PROBLEMS BRAVO",
      "yHEART PROBLEMS CHARLIE",
      "yHEART PROBLEMS DELTA",
      "yHEART PROBLEMS ECHO",
      "yHEMORRHAGE/LACERATION ALPHA",
      "yHEMORRHAGE/LACERATION BRAVO",
      "yHEMORRHAGE/LACERATION CHARLIE",
      "yHEMORRHAGE/LACERATION DELTA",
      "yHEMORRHAGE/LACERATION ECHO",
      "yOVERDOSE/POISONING ALPLHA",
      "yOVERDOSE/POISONING BRAVO",
      "yOVERDOSE/POISONING CHARLIE",
      "yOVERDOSE/POISONING DELTA",
      "yOVERDOSE/POISONING ECHO",
      "yPSYCHIATRIC/SUICIDE ALPHA",
      "yPSYCHIATRIC/SUICIDE BRAVO",
      "yPSYCHIATRIC/SUICIDE CHARLIE",
      "yPSYCHIATRIC/SUICIDE DELTA",
      "yPSYCHIATRIC/SUICIDE ECHO",
      "ySICK PERSON ALPHA",
      "ySICK PERSON BRAVO",
      "ySICK PERSON CHARLIE",
      "ySICK PERSON DELTA",
      "ySICK PERSON ECHO",
      "ySICK PERSON OMEGA",
      "ySTAB/GUNSHOT ALPHA",
      "ySTAB/GUNSHOT BRAVO",
      "ySTAB/GUNSHOT CHARLIE",
      "ySTAB/GUNSHOT DELTA",
      "ySTAB/GUNSHOT ECHO",
      "ySTROKE ALPHA",
      "ySTROKE BRAVO",
      "ySTROKE CHARLIE",
      "ySTROKE DELTA",
      "ySTROKE ECHO",
      "yTRAUMATIC INJURIES ALPHA",
      "yTRAUMATIC INJURIES BRAVO",
      "yTRAUMATIC INJURIES CHARLIE",
      "yTRAUMATIC INJURIES DELTA",
      "yTRAUMATIC INJURIES ECHO",
      "yUNCONSCIOUS/FAINTING ALPHA",
      "yUNCONSCIOUS/FAINTING BRAVO",
      "yUNCONSCIOUS/FAINTING CHARLIE",
      "yUNCONSCIOUS/FAINTING DELTA",
      "yUNCONSCIOUS/FAINTING ECHO",
      "yUNKNOWN PROBLEM ALPHA",
      "yUNKNOWN PROBLEM BRAVO",
      "yUNKNOWN PROBLEM CHARLIE",
      "yUNKNOWN PROBLEM DELTA",
      "yUNKNOWN PROBLEM ECHO",
      "ZCAD TESTING",

      "Alarm Business",
      "Alarm Residential",
      "Animal Control",
      "Armed Person",
      "Assault",
      "Assist/Another Agency",
      "Attempt To Locate",
      "Auto PD/No Injury",
      "B&E In Progress",
      "Call By Phone",
      "Cardiac/Respiratory",
      "Chase Vehicle/On Foot",
      "Child Custody",
      "City Ordinance Violation",
      "Civil Process",
      "Communicating Threats",
      "Damage To Property",
      "Dangerous Practice",
      "Direct Traffic",
      "Disorderly Conduct",
      "Domestic",
      "Followup",
      "Fraud/Forgery",
      "Harassment",
      "Hit&Run/No Injury",
      "Identity Theft",
      "Intoxicated Driver",
      "Intoxicated Person",
      "Investigation",
      "Keys Locked",
      "Larceny Past/Attempt",
      "Meet",
      "Missing Person/Runaway",
      "Narcotics Investigation",
      "Nine Hang Up/Open",
      "Noise Complaint",
      "Open Door/Window",
      "Other",
      "Overdose (L)",
      "Psychiatric/Mental",
      "Reckless Driver",
      "Recovered/Found Property",
      "Senior Check",
      "Sex Offense",
      "Special Assignment",
      "Special Enforcement",
      "Suspicious Person",
      "Suspicious Vehicle",
      "Traffic Stop",
      "Trespassing",
      "Unauthorized Use",
      "Unknown",
      "Vehicle Log",
      "Warrant",
      "Welfare Check"
  ));

}
