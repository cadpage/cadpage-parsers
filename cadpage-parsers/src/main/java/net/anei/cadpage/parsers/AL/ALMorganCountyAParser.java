
package net.anei.cadpage.parsers.AL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Morgan County, AL
 */

public class ALMorganCountyAParser extends DispatchOSSIParser {

  // The program string we want to use and nicely would solve everything is
  // PLACE? ADDR CALL! X? X? ID? UNIT? CH
  // But we can not do anything that simple because we want to avoid relying
  // on address and cross street fields to make decisions.
  //
  // The most reliable indicator is the ID field.  And if it is the 6th field
  // we can reliably conclude that the place and both cross fields are present

  // If that doesn't work, we will try to rely on the call description being
  // in position 2 or 3 to identify if the place field is present
  // If that doesn't fly, we will just have to depend on the main address
  // field to determine if we have a place field

  public ALMorganCountyAParser() {
    super("MORGAN COUNTY", "AL",

        // Odd variants
        "FYI? ( BUS_FIRE PLACE? ADDR/Z ID2 END " +

        // For the rest, the simple program string we want to use is
        // PLACE? ADDR CALL! X? X? ID? UNIT CH
        // But of course that will never work, so we have to do something
        // complicated

        // If we can identify the call field, things are pretty simple
             "| ADDR/Z X/Z PLACE CALL X/Z+? ( INFO | ID UNIT? | UNIT ) " +
             "| PLACE ADDR/Z CALL X/Z+? ( INFO | ID UNIT? | UNIT ) " +
             "| ADDR/Z CALL X/Z+? ( INFO | ID UNIT? | UNIT ) " +

             // If the call field is unknown, things get complicated
             // next see if the unit is in the 7th place, which means all
             // of the optional fields are present
             "| PLACE ADDR/Z CALL/Z X/Z X/Z ID/Z UNIT " +

             // Still no luck.  All we can do is hope the address
             // is recognizable
             "| PLACE? ADDR/s CALL! X/Z+? ( INFO | ID UNIT? | UNIT ) " +

             // And finally an optional channel at the end
             ") CH? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cad@morgan911.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BUS_FIRE")) return new CallField("BUSINESS FIRE", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("ID2")) return new IdField("20\\d{9}");
    if (name.equals("UNIT")) return new UnitField("(?!TAC|ATC)[A-Z]{3}[A-Z0-9]", true);
    if (name.equals("CH")) return new ChannelField("(?:TAC?|ATC) ?\\d{0,2}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!checkCall(field)) return false;
      super.parse(field, data);
      return true;
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      // If we encounter a 3rd cross street, bail out
      if (data.strCross.contains("&")) abort();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_PTN = Pattern.compile(".*[a-z].*");
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      if (!INFO_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "CH " + super.getFieldNames();
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("MARK SELBY PVT", "SELBY PVT");
    return super.adjustMapAddress(addr);
  }

  @Override
  public boolean checkCall(String field) {
    if (CALL_CAT_PTN.matcher(field).matches()) return true;
    Matcher match = CALL_SFX_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    return CALL_LIST.contains(field);

  }

  private static final Pattern CALL_CAT_PTN = Pattern.compile(".* CAT ?\\d");
  private static final Pattern CALL_SFX_PTN = Pattern.compile("(.*?) *- [A-EO]");
  private Set<String> CALL_LIST = new HashSet<String>(Arrays.asList(
      "ABDOMINAL PAIN / PROBLEMS",
      "ADVANCED SEND",
      "ALLERGIES / ENVENOMATIONS",
      "APPLIANCE FIRE",
      "ANIMAL BITES / ATTACKS",
      "ASSAULT/SEXUAL ASLT/STUNGUN",
      "ASSIST LAW ENFORCEMENT",
      "BACK PAIN",
      "BURNS / EXPLOSION",
      "BUSINESS FIRE",
      "BUSINESS FIRE ALARM",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "CAR FIRE",
      "CARBON MONOXIDE ALARM",
      "CARDIAC/RESPIRATORY/DEATH",
      "CAVE RESCUE",
      "CHEST PAIN / NON-TRAUMATIC",
      "CHOKING",
      "COMMERCIAL VEHICLE FIRE",
      "CONTROLLED BURN",
      "CONVULSIONS / SEIZURES",
      "DIABETIC PROBLEMS",
      "ELECTROCUTION / LIGHTNING",
      "EMERGENCY TRANSPORT",
      "EMS PR EVENT",
      "EXPLOSION",
      "EYE PROBLEMS / INJURIES",
      "FALLS",
      "FIRE DEPARTMENT CHECK",
      "FIRE DRILL (MOCK INCIDENT)",
      "FIRE OTHER",
      "FIRE PR EVENT",
      "GAS LEAK",
      "GAS SPILL",
      "GRASS FIRE",
      "HEADACHE",
      "HEART PROBLEMS / AICD",
      "HEAT / COLD EXPOSURE",
      "HEMORRHAGE / LACERATIONS",
      "HIT AND RUN NO INJURY",
      "HIT AND RUN WITH INJURY",
      "ILLEGAL BURN",
      "LIFT ASSIST",
      "MEDICAL CALL",
      "MEDICAL TRANSPORT",
      "OVERDOSE / POISONING",
      "PASSENGER VEHICLE FIRE",
      "PREGNANCY / CHILDBIRTH",
      "PSYCHIATRIC / SUICIDE ATT",
      "RESIDENTIAL FIRE",
      "RESIDENTIAL FIRE ALARM",
      "SEARCH AND RESCUE OPERATIONS",
      "SHOOTING",
      "SICK PERSON",
      "SMOKE INVESTIGATION",
      "STABBING",
      "STROKE (CVA) / TIA",
      "STRUCTURE FIRE",
      "SUICIDE",
      "SUICIDE ATTEMPTED",
      "SUICIDE THREATENED",
      "TRAFFIC INCIDENT ENTRAP",
      "TRAFFIC/TRANSPORT INCIDENT",
      "TRANSFER / INTERFACILITY",
      "TRASH FIRE",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS / FAINTING",
      "UNKNOWN / PERSON DOWN",
      "URGENT TRANSPORT",
      "WATER RESCUE WITH INJURY",
      "WEATHER RELATED",
      "WRECK",
      "WRECK NO INJURY",
      "WRECK WITH ENTRAPMENT",
      "WRECK WITH INJURY",
      "WRECK WITH UNKNOWN INJURY"
  ));
}
