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
      "BREATHING",
      "CAD TESTING COMPLAINT TYPE",
      "CHEST PAIN",
      "CONVULSIONS / SEIZURES",
      "DUMPSTER FIRE",
      "FALLS",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENCE",
      "GAS LEAK/SMELL",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEMORRHAGE/LACERATION",
      "MUTUAL AIDE ASSIST",
      "NEONATAL TRANSFER",
      "PREGNANCY/CHILDBIRTH",
      "PSYCHIATRIC/SUICIDE",
      "PUBLIC ASSIST",
      "SERVICE CALL",
      "SICK PERSON",
      "SMOKE REPORT/SCARE",
      "STANDBY/MOVE UP",
      "STROKE",
      "STRUCTURE FIRE COMMERCIAL",
      "TESTING ALARM",
      "TRAFFIC ACC",
      "TRAFFIC ACC (PIN/ENT/OT)",
      "TRANSFER/EMERGENCY",
      "TRANSFER/ROUTINE",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "yCHEST PAIN DELTA",
      "yFALLS ALPHA",
      "yFALLS BRAVO",
      "ySICK PERSON ALPHA",
      "ySTROKE CHARLIE",
      "yUNCONSCIOUS/FAINTING DELTA",
      "yUNKNOWN PROBLEM BRAVO",
      "ZCAD TESTING"
  ));
  
}
