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
          "ID? ADDR APT CH CITY ( X2 CALL UNIT! | X/Z CALL UNIT! | X X MAP INFO1 SKIP CALL! PLACENAME PHONE UNIT ) INFO+");
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

  private static final Set<String> CALL_LIST = new HashSet<>(Arrays.asList(
      "ALLERGIES",
      "ANIMAL RESCUE",
      "BREATHING",
      "CAD TESTING COMPLAINT TYPE",
      "CARBON MONOXIDE DETECTOR",
      "CARDIAC/RESPIRATORY",
      "CHEST PAIN",
      "CONTROL BURN",
      "CONVULSIONS / SEIZURES",
      "DUMPSTER FIRE",
      "EMS ASSIST",
      "EXPLOSION",
      "FALLS",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENCE",
      "FIRE ALARM RESIDENTIAL",
      "GAS LEAK/SMELL",
      "GRASS/WOODS FIRE",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEMORRHAGE/LACERATION",
      "ILLEGAL BURN",
      "LANDING ZONE",
      "MUTUAL AIDE ASSIST",
      "NEONATAL TRANSFER",
      "OVERDOSE/POISONING",
      "POWERLINE DOWN",
      "PREGNANCY/CHILDBIRTH",
      "PSYCHIATRIC/SUICIDE",
      "PUBLIC ASSIST",
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
      "TRANSFER/EMERGENCY",
      "TRANSFER/ROUTINE",
      "TRAUMATIC INJURIES",
      "TREE DOWN",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "VEHICLE FIRE COMMERCIAL",
      "VEHICLE FIRE PASSENGER",
      "yABDOMINAL ALPHA",
      "yABDOMINAL BRAVO",
      "yABDOMINAL CHARLIE",
      "yABDOMINAL ALPHA",
      "yALLERGIES BRAVO",
      "yALLERGIES CHARLIE",
      "yALLERGIES DELTA",
      "yALLERGIES DELTA",
      "yANIMAL BITES ALPHA",
      "yANIMAL BITES BRAVO",
      "yANIMAL BITES CHARLIE",
      "yANIMAL BITES DELTA",
      "yASSAULT/ RAPE ALPHA",
      "yASSAULT/ RAPE BRAVO",
      "yASSAULT/ RAPE CHARLIE",
      "yASSAULT/ RAPE DELTA",
      "yBACK PAIN CHARLIE",
      "yBREATHING CHARLIE",
      "yBREATHING DELTA",
      "yCARDIAC/RESPIRATORY ALPHA",
      "yCARDIAC/RESPIRATORY BRAVO",
      "yCARDIAC/RESPIRATORY CHARLIE",
      "yCARDIAC/RESPIRATORY DELTA",
      "yCARDIAC/RESPIRATORY ECHO",
      "yCONVULSIONS/SEIZURES ALPHA",
      "yCONVULSIONS/SEIZURES BRAVO",
      "yCONVULSIONS/SEIZURES CHARLIE",
      "yCONVULSIONS/SEIZURES DELTA",
      "yCHEST PAIN CHARLIE",
      "yCHEST PAIN DELTA",
      "yDIABETIC PROBLEMS ALPHA",
      "yDIABETIC PROBLEMS BRAVO",
      "yDIABETIC PROBLEMS CHARLIE",
      "yDIABETIC PROBLEMS DELTA",
      "yFALLS ALPHA",
      "yFALLS BRAVO",
      "yFALLS DELTA",
      "yHEADACHE ALPHA",
      "yHEADACHE BRAVE",
      "yHEADACHE CHARLIE",
      "yHEADACHE DELTA",
      "yHEART PROBLEMS CHARLIE",
      "yHEART PROBLEMS DELTA",
      "yHEMORRHAGE/LACERATION ALPHA",
      "yHEMORRHAGE/LACERATION BRAVO",
      "yHEMORRHAGE/LACERATION CHARLIE",
      "yHEMORRHAGE/LACERATION DELTA",
      "yOVERDOSE/POISONING ALPLHA",
      "yOVERDOSE/POISONING BRAVO",
      "yOVERDOSE/POISONING CHARLIE",
      "yOVERDOSE/POISONING DELTA",
      "yPSYCHIATRIC/SUICIDE ALPHA",
      "yPSYCHIATRIC/SUICIDE BRAVO",
      "yPSYCHIATRIC/SUICIDE CHARLIE",
      "yPSYCHIATRIC/SUICIDE DELTA",
      "ySICK PERSON ALPHA",
      "ySICK PERSON BRAVO",
      "ySICK PERSON CHARLIE",
      "ySICK PERSON DELTA",
      "ySICK PERSON OMEGA",
      "ySTROKE CHARLIE",
      "yTRAUMATIC INJURIES DELTA",
      "yUNCONSCIOUS/FAINTING DELTA",
      "yUNKNOWN PROBLEM ALPHA",
      "yUNKNOWN PROBLEM BRAVO",
      "yUNKNOWN PROBLEM CHARLIE",
      "yUNKNOWN PROBLEM DELTA",
      "ZCAD TESTING"
  ));

}
