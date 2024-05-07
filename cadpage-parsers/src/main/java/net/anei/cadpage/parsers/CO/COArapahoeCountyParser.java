package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

public class COArapahoeCountyParser extends FieldProgramParser {

  public COArapahoeCountyParser() {
    super("ARAPAHOE COUNTY", "CO",
          "( Resp._Info:MAP! ADDR ( GPS/d | GPS1/d GPS2/d ) APT APT PLACE CALL ID! " +
          "| Address_Changed:MAP! GPS/d ADDR2 PLACE CALL ID! " +
          "| Incident_Location_Changed_to:EMPTY! ID3 MAP ADDR GPS1 GPS2 EMPTY EMPTY PLACE CALL UNIT! " +
          "| Inc_Address_Update:ADDR! ID3 MAP GPS1 GPS2 EMPTY EMPTY PLACE " +
          "| ADDRESS_CHANGE:MAP! ADDR! UNIT! " +
          ") EMPTY? END");
    setupParseAddressFlags(FLAG_ALLOW_DUAL_DIRECTIONS);
    setupSpecialStreets("BROADWAY", "BROADWAY CIR");
  }

  @Override
  public String getFilter() {
    return "smfrrelay@smfra.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern PREFIX = Pattern.compile("(UNIVERSAL PRECAUTIONS(?:, SOB)?) *");
  private static final Pattern MASTER1 = Pattern.compile("RI:([A-Z]-\\d{2}-[A-Z](?:-[A-Z])?) (.*?) +([A-Z][,A-Z0-9]+)");
  private static final Pattern MASTER2 = Pattern.compile("([ A-Z]+) (?:-|RESPOND:?)(.*?)([A-Z]-\\d{2}-[A-Z](?:-[A-Z])?) (.*?)(?: Cmnd Chnl:(.*))?");
  private static final Pattern MASTER3 = Pattern.compile("ADDRESS CHANGE *([A-Z]-\\d{2}-[A-Z](?:-[A-Z])?) (.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("SMFR Dispatch Info:")) return false;

    Matcher match = PREFIX.matcher(body);
    String prefix = null;
    if (match.lookingAt()) {
      prefix = match.group(1);
      body = body.substring(match.end());
    }

    int pt = body.indexOf("\nDisclaimer:");
    if (pt >= 0) body = body.substring(0, pt).trim();

    body = body.replace("Resp.Info:", "Resp. Info:");
    body = body.replace("Resp. Info: |", "Resp. Info: ");

    // We have two different page formats
    // Check for the pipe delimited field format
    String[] flds = body.split("\\|", -1);
    if (flds.length >= 3) {
      if (!parseFields(flds, data)) return false;
    }

    // No go. Check for the undelimited field format
    else if ((match = MASTER1.matcher(body)).matches()) {

      setFieldList("MAP ADDR APT PLACE CALL UNIT");
      data.strMap = match.group(1);
      body = match.group(2).trim();
      data.strUnit = match.group(3);

      String call = CALL_LIST.getCode(body, true);
      if (call != null) body = body.substring(0,body.length()-call.length()).trim();
      parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS, body, data);
      if (call != null) {
        data.strCall = call;
        data.strPlace = getLeft();
      } else {
        data.strCall = getLeft();
        if (data.strCall.length() == 0) return false;
      }
    }

    else if ((match = MASTER2.matcher(body)).matches()) {
      setFieldList("UNIT CALL MAP ADDR APT PLACE CH");
      data.strUnit = match.group(1).trim();
      data.strCall = match.group(2).trim();
      data.strMap = match.group(3);
      parseAddress(StartType.START_ADDR, match.group(4), data);
      data.strPlace = getLeft();
      data.strChannel = getOptGroup(match.group(5));
    }

    else if ((match = MASTER3.matcher(body)).matches()) {
      setFieldList("MAP ADDR APT CALL");
      data.strMap = match.group(1);
      body = match.group(2).trim();
      String call = CALL_LIST.getCode(body);
      if (call != null) {
        data.strCall = call;
        body = body.substring(0, body.length()-call.length()).trim();
        parseAddress(body, data);
      } else {
        parseAddress(StartType.START_ADDR, body, data);
        data.strCall = getLeft();
        if (data.strCall.isEmpty()) return false;
      }
    }

    else return false;

    if (prefix != null) data.strCall = append(prefix, " - ", data.strCall);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("[A-Z]-\\d{2}-[A-Z](?:-[A-Z])?", true);
    if (name.equals("GPS")) return new GPSField("\\d{8,9} \\d{8,9}", true);
    if (name.equals("ID")) return new IdField("(?:Case ?# *)?(\\d\\d-[A-Z]{2,3}-\\d{6,7}|\\d\\d-\\d{7})", true);
    if (name.equals("ID3")) return new IdField("\\d{4}-\\d{7}", true);
    if (name.equals("ADDR2")) return new AddressField(".* TO: +(.*)");
    return super.getField(name);
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(
      "Abdominal Pain/Problems",
      "Air Alert 2 Inflight Emergency",
      "Alarm-CO No Sick Parties",
      "Alarm-Fire Alarm Commercial",
      "Alarm-Fire Alarm Residential",
      "Alarm-Medical Alarm",
      "Alcohol Evaluation",
      "Allergies/Envenomation",
      "Animal Bites/Attacks",
      "ARREST",
      "Assault/Sexual Assault",
      "Assist-Blood Draw",
      "Assist-Lift Assist",
      "Assist-Lock In (Child/Pet)",
      "Assist-Lock Out",
      "Assist-Other Agency Assist",
      "Assist-Police Assist",
      "Assist-Public Assist",
      "Assist-Water Problem/Shut Off",
      "Back Pain (Non-Traumatic)",
      "Breathing Problems",
      "Cardiac or Respiratory Arrest",
      "Chest Pain (Non-Traumatic)",
      "Convulsions/Seizures",
      "Diabetic Problems",
      "Driveway Eye Problems/ Injuries",
      "Electrical Hazard",
      "Falls",
      "Fire-Appliance Fire",
      "Fire-BBQ Grill Fire",
      "Fire-Brush Fire Large",
      "Fire-Brush Fire Small",
      "Fire-Commercial Carrier Fire",
      "Fire-Illegal Burn",
      "Fire-Outside Fire",
      "Fire-Vehicle Fire",
      "Fuel Spill Large",
      "Fuel Spill Small",
      "Gas-Commercial Leak",
      "Gas-Residential Leak",
      "HazMat",
      "Headache",
      "Heart Problems/ A.I.C.D",
      "Hemorrhage/Lacerations",
      "Invest-Lighting Strike",
      "Invest-Odor Commercial",
      "Invest-Odor Outside",
      "Invest-Odor Residential",
      "Invest-Smoke Inside",
      "Invest-Smoke Outside",
      "Line Down / Transformer",
      "MEDICAL",
      "Medical Assist",
      "MVA Extrication",
      "MVA Highway",
      "MVA Injuries",
      "MVA Motorcycle",
      "MVA Rollover",
      "MVA Unknown Injuries",
      "MVA Traffic Pedestrian Accidnt",
      "MVA Vehicle Into Building",
      "Overdose/Poisoning (Ingestion)",
      "Psych Problems",
      "Psych/Abn Behavior/Suicide Att",
      "Resc-Confined Space Rescue",
      "SF-Comm Str Fire Reported",
      "SF-Multi-Fam Str Fire Report",
      "SF-Outbuilding Fire",
      "SF-Res Structure Fire Reported",
      "SF1C-Commercial Str Fire",
      "Sick Person (Specific Diag)",
      "Stab/Gunshot/Penetrating Traum",
      "Standby In The Area",
      "Stroke(CVA)",
      "TECH RESCUE LEVEL 1",
      "TEST (Do not Dispatch)",
      "Test Call (Do Not Dispatch)",
      "Traffic Pedestrian Acciden",
      "Traumatic Injuries (Specific)",
      "Unconscious/Fainting (Near)",
      "Resc-Animal Rescue",
      "x1A-Abdominal Pain/Problems",
      "x21D-Hemorrhage/Lacerations",
      "x26A-Sick Person",
      "x9E-Cardiac/Resp Arrest"
  );
}
